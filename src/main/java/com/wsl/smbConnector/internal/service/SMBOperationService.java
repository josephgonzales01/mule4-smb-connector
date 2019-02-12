package com.wsl.smbConnector.internal.service;

import com.hierynomus.smbj.share.DiskShare;
import com.wsl.smbConnector.internal.SmbConnection;
import com.wsl.smbConnector.internal.parameters.CreateDirectoryParameters;
import com.wsl.smbConnector.internal.parameters.SMBFileParameters;
import com.wsl.smbConnector.internal.util.Utility;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SMBOperationService {

  private final Logger LOGGER = LoggerFactory.getLogger(SMBOperationService.class);


  public void createDirectory(SmbConnection connection, CreateDirectoryParameters parameters)
      throws IOException {
    //extract directories
    DiskShare diskShare = connection.getDiskShare();
    String targetPath = Utility.fixPath(parameters.getPath());
    List<String> parentDirs = Utility.listFileParentDirs(targetPath);

    if (parentDirs.isEmpty()) {
      //do nothing if no parent directory
      return;
    }
    LOGGER.info("Creating directory for {} at smb://{}/{}", targetPath, connection.getHost(),
        connection.getBaseDirectory());

    //****  create all directories one at a time
    StringBuilder currentDir = new StringBuilder(parentDirs.get(0));
    for (int i = 1; i <= parentDirs.size(); i++) {
      if (!diskShare.folderExists(currentDir.toString())) {
        diskShare.mkdir(currentDir.toString());
      }
      if (i < parentDirs.size()) {
        currentDir.append("/");
        currentDir.append(parentDirs.get(i));
      }
    }
    LOGGER.info("Directory Created: {}", currentDir);
  }


}
