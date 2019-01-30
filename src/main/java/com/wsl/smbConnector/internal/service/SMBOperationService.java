package com.wsl.smbConnector.internal.service;

import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.wsl.smbConnector.internal.SmbConnection;
import com.wsl.smbConnector.internal.parameters.*;

import java.io.IOException;
import java.util.List;

public interface SMBOperationService {

    public byte[] read(SmbConnection connection, FileReadParameters parameters) throws IOException;

    public void write(SmbConnection connection, FileWriteParameters parameters) throws IOException;

    public void move(SmbConnection connection, FileMoveParameters parameters) throws IOException;

    public void delete(SmbConnection connection, FileDeleteParameters parameters) throws IOException;

    public void rename(SmbConnection connection, FileRenameParameters parameters) throws IOException;

    public List<FileIdBothDirectoryInformation> listFiles(SmbConnection connection, String path) throws IOException;

    public void createDirectory(SmbConnection connection, CreateDirectoryParameters parameters) throws IOException;
}
