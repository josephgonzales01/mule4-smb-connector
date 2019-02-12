package com.wsl.smbConnector.internal.service;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.fileinformation.FileAllInformation;
import com.hierynomus.msfscc.fileinformation.FileBasicInformation;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;
import com.wsl.smbConnector.api.SmbFileAttributes;
import com.wsl.smbConnector.api.TimeInfo;
import com.wsl.smbConnector.internal.SmbConnection;
import com.wsl.smbConnector.internal.parameters.SMBFileParameters;
import com.wsl.smbConnector.internal.util.Utility;
import java.io.IOException;
import java.util.EnumSet;
import org.apache.commons.io.IOUtils;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMBReadOperationService extends SMBOperationService {

  private final Logger LOGGER = LoggerFactory.getLogger(SMBReadOperationService.class);


  public Result<byte[], SmbFileAttributes> perform(SmbConnection connection,
      SMBFileParameters parameters)
      throws IOException {

    DiskShare diskShare = connection.getDiskShare();
    String targetPath = Utility.fixPath(parameters.getPath());
    File file = diskShare
        .openFile(targetPath, EnumSet.of(AccessMask.GENERIC_READ), null, SMB2ShareAccess.ALL,
            SMB2CreateDisposition.FILE_OPEN, null);
    LOGGER.info("Reading file at smb://{}/{}/{}", connection.getHost(),
        connection.getBaseDirectory(), targetPath);

    FileBasicInformation fileInfo = file.getFileInformation().getBasicInformation();

    SmbFileAttributes attributes = new SmbFileAttributes(Utility.getFilename(targetPath),
        file.getFileInformation().getStandardInformation().getEndOfFile(),
        new TimeInfo(fileInfo.getCreationTime().toString(), fileInfo.getLastAccessTime().toString(),
            fileInfo.getLastWriteTime().toString(), fileInfo.getChangeTime().toString()));

    LOGGER.info("Extracted File attributes: {}", attributes);

    byte[] data = IOUtils.toByteArray(file.getInputStream());
    file.close();
    return Result.<byte[], SmbFileAttributes>builder().output(data).attributes(attributes).build();

  }


}
