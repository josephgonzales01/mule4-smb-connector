package com.wsl.smbConnector.internal.service;

import com.wsl.smbConnector.internal.SmbConnection;
import com.wsl.smbConnector.internal.api.payload.SmbFileAttributes;
import com.wsl.smbConnector.internal.parameters.*;
import org.mule.runtime.extension.api.runtime.operation.Result;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SMBOperationService {

    public byte[] read(SmbConnection connection, FileReadParameters parameters) throws IOException;

    public void write(SmbConnection connection, FileWriteParameters parameters) throws IOException;

    public void move(SmbConnection connection, FileMoveParameters parameters) throws IOException;

    public void delete(SmbConnection connection, FileDeleteParameters parameters) throws IOException;

    public void rename(SmbConnection connection, FileRenameParameters parameters) throws IOException;

    public List<Result<Map<String, Object>, SmbFileAttributes>> list(SmbConnection connection, ListDirectoryParameters parameters) throws IOException;

    public void createDirectory(SmbConnection connection, CreateDirectoryParameters parameters) throws IOException;
}
