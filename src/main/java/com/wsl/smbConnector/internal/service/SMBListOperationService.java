package com.wsl.smbConnector.internal.service;

import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.protocol.commons.EnumWithValue;
import com.hierynomus.smbj.share.DiskShare;
import com.wsl.smbConnector.internal.SmbConnection;
import com.wsl.smbConnector.internal.api.payload.SmbFileAttributes;
import com.wsl.smbConnector.internal.api.payload.TimeInfo;
import com.wsl.smbConnector.internal.parameters.ListDirectoryMode;
import com.wsl.smbConnector.internal.parameters.ListDirectoryParameters;
import com.wsl.smbConnector.internal.parameters.SMBFileParameters;
import com.wsl.smbConnector.internal.util.Utility;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMBListOperationService extends SMBOperationService {

  private final Logger LOGGER = LoggerFactory.getLogger(SMBListOperationService.class);


  public List<Result<Map<String, Object>, SmbFileAttributes>> perform(SmbConnection connection,
      SMBFileParameters param)
      throws IOException {

    DiskShare diskShare = connection.getDiskShare();

    ListDirectoryParameters parameters = (ListDirectoryParameters) param;

    String dirPath = diskShare.getFileInformation(parameters.getPath()).getStandardInformation()
        .isDirectory() ? parameters.getPath()
        : Utility.listFileParentDirs(parameters.getPath()).stream()
            .collect(Collectors.joining("/"));

    LOGGER
        .info("Listing file at smb://{}/{}/{}", connection.getHost(), connection.getBaseDirectory(),
            dirPath);

    List<FileIdBothDirectoryInformation> contents =
        parameters.getSearchPattern().isEmpty() ? diskShare.list(dirPath)
            : diskShare.list(dirPath, parameters.getSearchPattern());

    return contents.stream().filter(d -> {
      //exclude .. hardlink files
      if (".".equals(d.getFileName()) || "..".equals(d.getFileName())) {
        return false;
      }
      boolean isDirectory = EnumWithValue.EnumUtils
          .isSet(d.getFileAttributes(), FileAttributes.FILE_ATTRIBUTE_DIRECTORY);

     return parameters.getListMode() == ListDirectoryMode.DIRECTORY_ONLY ? isDirectory
          : parameters.getListMode() == ListDirectoryMode.FILE_ONLY ? !isDirectory : true;

    }).map(d -> {
      SmbFileAttributes attr = new SmbFileAttributes(
          d.getFileName(),
          d.getEndOfFile(),
          new TimeInfo(d.getCreationTime().toString(), d.getLastAccessTime().toString(),
              d.getLastWriteTime().toString(), d.getChangeTime().toString()));

      return Result.<Map<String, Object>, SmbFileAttributes>builder().output(attr.getHashMap())
          .attributes(attr).build();

    }).collect(Collectors.toList());
  }

}
