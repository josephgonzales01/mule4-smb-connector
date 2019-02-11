package com.wsl.smbConnector.internal.service;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;
import com.wsl.smbConnector.internal.SmbConnection;
import com.wsl.smbConnector.internal.parameters.CreateDirectoryParameters;
import com.wsl.smbConnector.internal.parameters.FileWriteMode;
import com.wsl.smbConnector.internal.parameters.FileWriteParameters;
import com.wsl.smbConnector.internal.parameters.SMBFileParameters;
import com.wsl.smbConnector.internal.util.Utility;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMBWriteOperationService extends SMBOperationService {

  private final Logger LOGGER = LoggerFactory.getLogger(SMBWriteOperationService.class);



  public void perform(SmbConnection connection, SMBFileParameters params)
      throws IOException {

    FileWriteParameters parameters = (FileWriteParameters) params;
    DiskShare diskShare = connection.getDiskShare();
    String targetPath = Utility.fixPath(parameters.getPath());

    LOGGER.info("Writing file attributes isCreateDirectory:{} | writeMode:{}",
        parameters.isCreateDirectory(), parameters.getWriteMode().name());

    //create non existing directory
    if (parameters.isCreateDirectory() && !diskShare.folderExists(targetPath)) {
      createDirectory(connection, new CreateDirectoryParameters(targetPath));
    }

    boolean fileExist = diskShare.fileExists(targetPath);

    Set accessMask = new HashSet<>(
        Arrays.asList(AccessMask.MAXIMUM_ALLOWED, AccessMask.GENERIC_WRITE));
    SMB2CreateDisposition createDisposition = SMB2CreateDisposition.FILE_CREATE;
    Set createOptions = new HashSet<>(Arrays.asList(SMB2CreateOptions.FILE_NON_DIRECTORY_FILE));
    boolean useWriteOffset = false;

    if (fileExist && parameters.getWriteMode() == FileWriteMode.OVERWRITE) {
      createDisposition = SMB2CreateDisposition.FILE_OVERWRITE_IF;
    } else if (fileExist && parameters.getWriteMode() == FileWriteMode.APPEND) {
      accessMask.add(AccessMask.FILE_APPEND_DATA);
      createDisposition = SMB2CreateDisposition.FILE_OVERWRITE_IF;
      useWriteOffset = true;
    } else if (fileExist && parameters.getWriteMode() == FileWriteMode.PREPEND) {
      createDisposition = SMB2CreateDisposition.FILE_OPEN_IF;
      createOptions.add(SMB2CreateOptions.FILE_RANDOM_ACCESS);
    } else {
      accessMask.add(AccessMask.FILE_ADD_FILE);
    }
    //String existinData = fileExist && parameters.getWriteMode() == FileWriteMode.PREPEND ? new String(read(connection, new FileReadParameters(targetPath))) : "";
    File file = diskShare
        .openFile(targetPath, accessMask, null, SMB2ShareAccess.ALL, createDisposition,
            createOptions);

    StringBuilder newData = new StringBuilder(parameters.getContent());
    //newData.append(existinData);

    LOGGER
        .info("Writing file at smb://{}/{}/{}", connection.getHost(), connection.getBaseDirectory(),
            targetPath);

    if (useWriteOffset) {
      long offset = file.getFileInformation().getStandardInformation().getEndOfFile();
      file.write(newData.toString().getBytes(), offset);

    } else {
      file.write(newData.toString().getBytes(), 0);
    }
    file.close();

  }
}
