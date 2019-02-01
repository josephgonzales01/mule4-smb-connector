package com.wsl.smbConnector.internal;

import com.wsl.smbConnector.internal.api.payload.SmbFileAttributes;
import com.wsl.smbConnector.internal.parameters.*;
import com.wsl.smbConnector.internal.service.SMBOperationService;
import com.wsl.smbConnector.internal.service.SMBOperationServiceImpl;
import org.mule.runtime.extension.api.runtime.operation.Result;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SmbConnectionTestMain {
    public static final String DOMAIN = "";
    public static final String BASE_DIRECTORY = "smb_shared";
    public static final String HOST = "5-15-14-135.residential.rdsnet.ro";
    public static final String USERNAME = "";
    public static final String PASS = "";

    private SmbConnection connection;
    private SMBOperationService service = new SMBOperationServiceImpl();

    public SmbConnectionTestMain() throws IOException {
        connection = new SmbConnection("", DOMAIN, BASE_DIRECTORY, HOST, USERNAME, PASS, SmbConnection.DEFAULT_TIMEOUT);
    }

    public void read() throws IOException {
        byte[] read = service.read(connection, new FileReadParameters("dir0//dir1\\dir2/file1.txt"));
        System.out.println("Data read: " + new String(read));
    }

    public void write() throws IOException {

        service.write(connection, new FileWriteParameters("dir0/dir1/dir2/file1.txt", "<EOF>", FileWriteMode.APPEND, true));
    }

    public void list() throws IOException {
        List<Result<Map<String, Object>, SmbFileAttributes>> results = service.list(connection, new ListDirectoryParameters("", "", ListDirectoryMode.FILE_ONLY));
        for (Result r : results) {
            System.out.println("result: " + r);
        }
    }

    public void createDirectory() throws IOException {
        service.createDirectory(connection, new CreateDirectoryParameters("dir0//dir1\\dir2/file1.txt"));

    }

    public void delete() throws IOException {
        service.delete(connection, new FileDeleteParameters("dir0//dir1\\dir2/file1.txt", true));
    }

    public void move() throws IOException {
        service.move(connection, new FileMoveParameters("dir0/dir1/dir2/file1.txt", "dir0/dir1/dir2/dir3/file1.txt", true, true, true));
    }

    public void rename() throws IOException {
        service.rename(connection, new FileRenameParameters("dirx/new_file.txt", "dirx/new_file5.txt", true));
    }

    public static void main(String[] args) {

        try {
            SmbConnectionTestMain connectionTest = new SmbConnectionTestMain();
            //connectionTest.list();
            //connectionTest.read();
            //connectionTest.createDirectory();
            //connectionTest.write();
            //connectionTest.delete();
            //connectionTest.move();
           connectionTest.rename();
            //System.out.println("filename: "+ Utility.getFilename("file2.txt"));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
