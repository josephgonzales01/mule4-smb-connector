package com.wsl.smbConnector.internal;

import com.wsl.smbConnector.api.SmbFileAttributes;
import com.wsl.smbConnector.internal.parameters.CreateDirectoryParameters;
import com.wsl.smbConnector.internal.parameters.FileDeleteParameters;
import com.wsl.smbConnector.internal.parameters.FileMoveParameters;
import com.wsl.smbConnector.internal.parameters.FileReadParameters;
import com.wsl.smbConnector.internal.parameters.FileRenameParameters;
import com.wsl.smbConnector.internal.parameters.FileWriteMode;
import com.wsl.smbConnector.internal.parameters.FileWriteParameters;
import com.wsl.smbConnector.internal.parameters.ListDirectoryMode;
import com.wsl.smbConnector.internal.parameters.ListDirectoryParameters;
import com.wsl.smbConnector.internal.service.SMBDeleteOperationService;
import com.wsl.smbConnector.internal.service.SMBListOperationService;
import com.wsl.smbConnector.internal.service.SMBMoveOperationService;
import com.wsl.smbConnector.internal.service.SMBReadOperationService;
import com.wsl.smbConnector.internal.service.SMBRenameOperationService;
import com.wsl.smbConnector.internal.service.SMBWriteOperationService;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmbConnectionTestMain {

  public static final String DOMAIN = "";
  public static final String BASE_DIRECTORY = "smb_shared";
  public static final String HOST = "192.168.1.5";
  public static final String USERNAME = "";
  public static final String PASS = "";

  private SmbConnection connection;
  private SMBReadOperationService readService;
  private SMBWriteOperationService writeService;
  private SMBRenameOperationService renameService;
  private SMBMoveOperationService moveService;
  private SMBDeleteOperationService deleteService;
  private SMBListOperationService listService;

  Logger LOGGER = LoggerFactory.getLogger(SmbConnectionTestMain.class);


  public SmbConnectionTestMain() throws IOException {
    connection = new SmbConnection("", DOMAIN, BASE_DIRECTORY, HOST, USERNAME, PASS,
        SmbConnection.DEFAULT_TIMEOUT);
    readService = new SMBReadOperationService();
    writeService = new SMBWriteOperationService();
    renameService = new SMBRenameOperationService();
    moveService = new SMBMoveOperationService();
    deleteService = new SMBDeleteOperationService();
    listService = new SMBListOperationService();

  }

  public void read() throws IOException {
    Result<byte[], Void> result = readService
        .perform(connection, new FileReadParameters("new_file2.csv"));
    String read = new String((byte[]) result.getOutput());
    LOGGER.info("Data read stream: " + read);

  }

  public void write() throws IOException {

    writeService.perform(connection,
        new FileWriteParameters("dirx/new_file5.txt", IOUtils.toInputStream("1234", Charset.defaultCharset()), FileWriteMode.PREPEND, true));
  }

  public void list() throws IOException {
    List<Result<Map<String, Object>, SmbFileAttributes>> results =
        listService
            .perform(connection, new ListDirectoryParameters("", "", ListDirectoryMode.FILE_ONLY));
    for (Result r : results) {
      LOGGER.info("result: " + r.getOutput());
    }

  }

  public void createDirectory() throws IOException {

    writeService
        .createDirectory(connection, new CreateDirectoryParameters("dir0//dir1\\dir2/file1.txt"));

  }

  public void delete() throws IOException {
    deleteService
        .perform(connection, new FileDeleteParameters("dir0//dir1\\dir2/file1.txt", true));
  }

  public void move() throws IOException {
    moveService.perform(connection,
        new FileMoveParameters("dir0/dir1/dir2/file1.txt", "dir0/dir1/dir2/dir3/file1.txt", true,
            true, true));
  }

  public void rename() throws IOException {
    renameService.perform(connection,
        new FileRenameParameters("dirx/new_file.txt", "dirx/new_file5.txt", true));
  }

  public static void main(String[] args) {

    try {
      SmbConnectionTestMain connectionTest = new SmbConnectionTestMain();
      //connectionTest.list();
      connectionTest.read();
      //connectionTest.createDirectory();
      //connectionTest.write();
      //connectionTest.delete();
      //connectionTest.move();
      //connectionTest.rename();
      //System.out.println("filename: "+ Utility.getFilename("file2.txt"));

    } catch (IOException e) {
      e.printStackTrace();
    }


  }


}
