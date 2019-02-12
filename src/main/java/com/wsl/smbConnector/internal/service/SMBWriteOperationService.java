package com.wsl.smbConnector.internal.service;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;
import com.wsl.smbConnector.internal.SmbConnection;
import com.wsl.smbConnector.internal.parameters.CreateDirectoryParameters;
import com.wsl.smbConnector.internal.parameters.FileReadParameters;
import com.wsl.smbConnector.internal.parameters.FileWriteMode;
import com.wsl.smbConnector.internal.parameters.FileWriteParameters;
import com.wsl.smbConnector.internal.parameters.SMBFileParameters;
import com.wsl.smbConnector.internal.util.Utility;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMBWriteOperationService extends SMBOperationService {

  private final Logger LOGGER = LoggerFactory.getLogger(SMBWriteOperationService.class);


  public void perform(SmbConnection connection, SMBFileParameters params)
      throws IOException {

    FileWriteParameters parameters = (FileWriteParameters) params;
    DiskShare diskShare = connection.getDiskShare();
    String targetPath = Utility.fixPath(parameters.getPath());

    LOGGER.info("Write to file attributes isCreateDirectory:{} | writeMode:{}",
        parameters.isCreateDirectory(), parameters.getWriteMode().name());

    //create non existing directory
    if (parameters.isCreateDirectory() && !diskShare.folderExists(targetPath)) {
      createDirectory(connection, new CreateDirectoryParameters(targetPath));
    }

    if (diskShare.fileExists(targetPath)) {
      writeToExistingFile(connection, parameters);
      return;
    }

    Set accessMask = new HashSet<>(
        Arrays.asList(AccessMask.MAXIMUM_ALLOWED, AccessMask.GENERIC_WRITE,
            AccessMask.FILE_ADD_FILE));
    Set createOptions = new HashSet<>(Arrays.asList(SMB2CreateOptions.FILE_NON_DIRECTORY_FILE));

    File file = diskShare
        .openFile(targetPath, accessMask, null, SMB2ShareAccess.ALL,
            SMB2CreateDisposition.FILE_CREATE, createOptions);

    LOGGER
        .info("Writing file at smb://{}/{}/{}", connection.getHost(), connection.getBaseDirectory(),
            targetPath);
    file.write(IOUtils.toByteArray(parameters.getContent()), 0);
    file.close();
  }


  private void writeToExistingFile(SmbConnection connection, FileWriteParameters parameters)
      throws IOException {

    String targetPath = Utility.fixPath(parameters.getPath());
    Set accessMask = new HashSet<>(
        Arrays.asList(AccessMask.MAXIMUM_ALLOWED, AccessMask.GENERIC_WRITE));
    SMB2CreateDisposition createDisposition = SMB2CreateDisposition.FILE_OVERWRITE_IF;
    Set createOptions = new HashSet<>(Arrays.asList(SMB2CreateOptions.FILE_NON_DIRECTORY_FILE));

    FileWriteMode mode = parameters.getWriteMode();

    if (mode == FileWriteMode.APPEND) {
      accessMask.add(AccessMask.FILE_APPEND_DATA);
      createDisposition = SMB2CreateDisposition.FILE_OPEN_IF;
    }

    File file = connection.getDiskShare()
        .openFile(targetPath, accessMask, null, SMB2ShareAccess.ALL, createDisposition,
            createOptions);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    outputStream.write(IOUtils.toByteArray(parameters.getContent()));

    LOGGER
        .info("Writing file at smb://{}/{}/{}", connection.getHost(), connection.getBaseDirectory(),
            targetPath);

    if (mode == FileWriteMode.PREPEND) {
      //add existing data to end of file
      Result<byte[], Void> result = new SMBReadOperationService()
          .perform(connection, new FileReadParameters(targetPath));
      outputStream.write(result.getOutput());
      file.write(outputStream.toByteArray(), 0);
    } else {
      long offset = file.getFileInformation().getStandardInformation().getEndOfFile();
      file.write(outputStream.toByteArray(), offset);
    }

    file.close();
  }
}
