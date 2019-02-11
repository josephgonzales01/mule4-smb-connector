package com.wsl.smbConnector.internal.service;

import com.hierynomus.smbj.share.DiskShare;
import com.wsl.smbConnector.internal.SmbConnection;
import com.wsl.smbConnector.internal.parameters.FileDeleteParameters;
import com.wsl.smbConnector.internal.parameters.SMBFileParameters;
import com.wsl.smbConnector.internal.util.Utility;
import java.io.IOException;
import org.slf4j.LoggerFactory;

public class SMBDeleteOperationService extends SMBOperationService {

  private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SMBDeleteOperationService.class);

  public void perform(SmbConnection connection, SMBFileParameters parameters)
      throws IOException {

    DiskShare diskShare = connection.getDiskShare();
    FileDeleteParameters param = (FileDeleteParameters) parameters;
    String targetPath = Utility.fixPath(param.getPath());
    if (diskShare.getFileInformation(targetPath).getStandardInformation().isDirectory()) {
      diskShare.rmdir(targetPath, param.isRecursive());
    } else {
      diskShare.rm(targetPath);
    }

    LOGGER.info("Deleting smb://{}/{}/{}", connection.getHost(), connection.getBaseDirectory(),
        targetPath);
  }
}
