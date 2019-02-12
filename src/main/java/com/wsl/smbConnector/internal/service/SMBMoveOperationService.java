package com.wsl.smbConnector.internal.service;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;
import com.wsl.smbConnector.internal.SmbConnection;
import com.wsl.smbConnector.internal.parameters.CreateDirectoryParameters;
import com.wsl.smbConnector.internal.parameters.FileMoveParameters;
import com.wsl.smbConnector.internal.parameters.FileReadParameters;
import com.wsl.smbConnector.internal.parameters.SMBFileParameters;
import com.wsl.smbConnector.internal.util.Utility;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMBMoveOperationService extends SMBOperationService {

  private final Logger LOGGER = LoggerFactory.getLogger(SMBListOperationService.class);

  private SMBReadOperationService readService;

  public SMBMoveOperationService() {
    readService = new SMBReadOperationService();
  }

  public void perform(SmbConnection connection, SMBFileParameters param) throws IOException {
    DiskShare diskShare = connection.getDiskShare();
    FileMoveParameters parameters = (FileMoveParameters) param;

    String sourcePath = Utility.fixPath(parameters.getPath());
    String targetPath = Utility.fixPath(parameters.getTargetPath());

    Set accessMask = new HashSet<>(
        Arrays.asList(AccessMask.GENERIC_WRITE, AccessMask.FILE_ADD_FILE));
    SMB2CreateDisposition createDisposition =
        parameters.isOverwrite() ? SMB2CreateDisposition.FILE_SUPERSEDE
            : SMB2CreateDisposition.FILE_CREATE;

    LOGGER.info("Moving file attributes isOverwrite:{} | isCreateDirectory:{} | isRemoveSource:{}",
        parameters.isOverwrite(), parameters.isCreateDirectory(), parameters.isRemoveSource());

    //create non existing directory
    if (parameters.isCreateDirectory() && !diskShare.folderExists(targetPath)) {
      createDirectory(connection, new CreateDirectoryParameters(targetPath));
    }

    File targetFile = diskShare
        .openFile(targetPath, accessMask, null, SMB2ShareAccess.ALL, createDisposition,
            EnumSet.of(SMB2CreateOptions.FILE_NON_DIRECTORY_FILE));

    Result<byte[], Void> result = readService
        .perform(connection, new FileReadParameters(sourcePath));

    LOGGER.info("Transferring file to smb://{}/{}/{}", connection.getHost(),
        connection.getBaseDirectory(), targetPath);
    targetFile.write((byte[]) result.getOutput(), 0);
    if (parameters.isRemoveSource()) {
      LOGGER
          .info("Removing file smb://{}/{}/{}", connection.getHost(), connection.getBaseDirectory(),
              sourcePath);

      diskShare.rm(sourcePath);

    }
  }


}
