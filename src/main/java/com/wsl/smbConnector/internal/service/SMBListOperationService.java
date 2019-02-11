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


  public void perform(SmbConnection connection, SMBFileParameters param, List<Result<Map<String, Object>, SmbFileAttributes>> result)
      throws IOException {

    DiskShare diskShare = connection.getDiskShare();
    List<FileIdBothDirectoryInformation> contents;

    ListDirectoryParameters parameters = (ListDirectoryParameters) param;

    //check if the given target path is a file or directory
    if (diskShare.getFileInformation(parameters.getPath()).getStandardInformation()
        .isDirectory()) {
      contents =
          parameters.getSearchPattern().isEmpty() ? diskShare.list(parameters.getPath())
              : diskShare.list(parameters.getPath(), parameters.getSearchPattern());
    } else {
      String path = Utility.listFileParentDirs(parameters.getPath()).stream()
          .collect(Collectors.joining("/"));
      contents = parameters.getSearchPattern().isEmpty() ? diskShare.list(path)
          : diskShare.list(path, parameters.getSearchPattern());
    }

    contents.stream().filter(d -> {
      if (".".equals(d.getFileName()) || "..".equals(d.getFileName())) {
        return false;
      }
      if (parameters.getListMode() == ListDirectoryMode.ALL) {
        return true;
      } else if (parameters.getListMode() == ListDirectoryMode.FILE_ONLY) {
        return !EnumWithValue.EnumUtils
            .isSet(d.getFileAttributes(), FileAttributes.FILE_ATTRIBUTE_DIRECTORY);
      } else {
        return EnumWithValue.EnumUtils
            .isSet(d.getFileAttributes(), FileAttributes.FILE_ATTRIBUTE_DIRECTORY);
      }
    }).map(d -> {
      SmbFileAttributes attr = new SmbFileAttributes(
          d.getFileName(),
          d.getEndOfFile(),
          new TimeInfo(d.getCreationTime().toString(), d.getLastAccessTime().toString(),
              d.getLastWriteTime().toString(), d.getChangeTime().toString()));

      return Result.<Map<String, Object>, SmbFileAttributes>builder().output(attr.getHashMap())
          .attributes(attr).build();

    }).forEach(result::add);

  }

}
