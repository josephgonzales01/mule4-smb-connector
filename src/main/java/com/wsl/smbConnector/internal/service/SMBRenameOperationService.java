package com.wsl.smbConnector.internal.service;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;
import com.wsl.smbConnector.internal.SmbConnection;
import com.wsl.smbConnector.internal.parameters.FileRenameParameters;
import com.wsl.smbConnector.internal.parameters.SMBFileParameters;
import com.wsl.smbConnector.internal.util.Utility;
import java.io.IOException;
import java.util.EnumSet;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMBRenameOperationService extends SMBOperationService {

  private final Logger LOGGER = LoggerFactory.getLogger(SMBRenameOperationService.class);


  public void perform(SmbConnection connection, SMBFileParameters params)
      throws IOException {

    FileRenameParameters parameters = (FileRenameParameters) params;
    DiskShare diskShare = connection.getDiskShare();
    String sourcePath = Utility.fixPath(parameters.getPath());

    //always use the source parent directories for the new name
    StringBuilder newName = new StringBuilder(
        Utility.listFileParentDirs(sourcePath).stream().collect(Collectors.joining("/")));
    newName.append('\\');
    newName.append(Utility.getFilename(parameters.getNewName()));

    LOGGER.info("Renaming file smb://{}/{}/{} to {}", connection.getHost(),
        connection.getBaseDirectory(), sourcePath, newName);
    SMB2CreateDisposition createDisposition = SMB2CreateDisposition.FILE_OPEN;

    File renameFile = diskShare
        .openFile(sourcePath, EnumSet.of(AccessMask.DELETE, AccessMask.GENERIC_WRITE), null,
            SMB2ShareAccess.ALL, createDisposition,
            EnumSet.of(SMB2CreateOptions.FILE_NON_DIRECTORY_FILE));
    renameFile.rename(newName.toString(), parameters.isOverwrite());

    renameFile.close();

  }

}
