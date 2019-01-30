package com.wsl.smbConnector.internal.service;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;
import com.wsl.smbConnector.internal.SmbConnection;
import com.wsl.smbConnector.internal.parameters.*;
import com.wsl.smbConnector.internal.util.Utility;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SMBOperationServiceImpl implements SMBOperationService {

    private final Logger LOGGER = LoggerFactory.getLogger(SMBOperationServiceImpl.class);

    @Override
    public byte[] read(SmbConnection connection, FileReadParameters parameters) throws IOException {
        DiskShare diskShare = connection.getDiskShare();
        String targetPath = Utility.fixPath(parameters.getTargetPath());
        try (File file = diskShare.openFile(targetPath, EnumSet.of(AccessMask.GENERIC_READ), null, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN, null)) {
            LOGGER.info("Reading file at smb://{}/{}/{}", connection.getHost(), connection.getBaseDirectory(), targetPath);
            return IOUtils.toByteArray(file.getInputStream());
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public void write(SmbConnection connection, FileWriteParameters parameters) throws IOException {
        DiskShare diskShare = connection.getDiskShare();
        String targetPath = Utility.fixPath(parameters.getTargetPath());

        LOGGER.info("Writing file attributes isCreateDirectory:{} | writeMode:{}", parameters.isCreateDirectory(), parameters.getWriteMode().name());

        //create non existing directory
        if (parameters.isCreateDirectory() && !diskShare.folderExists(targetPath)) {
            createDirectory(connection, new CreateDirectoryParameters(targetPath));
        }

        boolean fileExist = diskShare.fileExists(targetPath);

        Set accessMask = new HashSet<>(Arrays.asList(AccessMask.MAXIMUM_ALLOWED, AccessMask.GENERIC_WRITE));
        SMB2CreateDisposition createDisposition = SMB2CreateDisposition.FILE_CREATE;
        Set createOptions = new HashSet<>(Arrays.asList(SMB2CreateOptions.FILE_NON_DIRECTORY_FILE));
        boolean useWriteOffset = false;

        if (fileExist && parameters.getWriteMode() == FileWriteMode.OVERWRITE) {
            createDisposition = SMB2CreateDisposition.FILE_SUPERSEDE;
        } else if (fileExist && parameters.getWriteMode() == FileWriteMode.APPEND) {
            accessMask.add(AccessMask.FILE_APPEND_DATA);
            createDisposition = SMB2CreateDisposition.FILE_OPEN_IF;
            useWriteOffset = true;
        } else if (fileExist && parameters.getWriteMode() == FileWriteMode.PREPEND) {
            createDisposition = SMB2CreateDisposition.FILE_SUPERSEDE;
        } else {
            accessMask.add(AccessMask.FILE_ADD_FILE);
        }
        String existinData = fileExist && parameters.getWriteMode() == FileWriteMode.PREPEND ? new String(read(connection, new FileReadParameters(targetPath))) : "";
        File file = diskShare.openFile(targetPath, accessMask, null, SMB2ShareAccess.ALL, createDisposition, createOptions);

        StringBuilder newData = new StringBuilder(parameters.getContent());
        newData.append(existinData);

        LOGGER.info("Writing file at smb://{}/{}/{}", connection.getHost(), connection.getBaseDirectory(), targetPath);

        if (useWriteOffset) {
            long offset = file.getFileInformation().getStandardInformation().getEndOfFile();
            file.write(newData.toString().getBytes(), offset);
        } else {
            file.write(newData.toString().getBytes(), 0);
        }
        file.close();

    }

    @Override
    public void move(SmbConnection connection, FileMoveParameters parameters) throws IOException {
        DiskShare diskShare = connection.getDiskShare();
        String sourcePath = Utility.fixPath(parameters.getSourcePath());
        String targetPath = Utility.fixPath(parameters.getTargetPath());

        Set accessMask = new HashSet<>(Arrays.asList(AccessMask.GENERIC_WRITE, AccessMask.FILE_ADD_FILE));
        SMB2CreateDisposition createDisposition = parameters.isOverwrite() ? SMB2CreateDisposition.FILE_SUPERSEDE : SMB2CreateDisposition.FILE_CREATE;

        LOGGER.info("Moving file attributes isOverwrite:{} | isCreateDirectory:{} | isRemoveSource:{}", parameters.isOverwrite(), parameters.isCreateDirectory(), parameters.isRemoveSource());

        //create non existing directory
        if (parameters.isCreateDirectory() && !diskShare.folderExists(targetPath)) {
            createDirectory(connection, new CreateDirectoryParameters(targetPath));
        }

        File targetFile = diskShare.openFile(targetPath, accessMask, null, SMB2ShareAccess.ALL, createDisposition, EnumSet.of(SMB2CreateOptions.FILE_NON_DIRECTORY_FILE));

        byte[] datadToCopy = read(connection, new FileReadParameters(sourcePath));

        LOGGER.info("Transferring file to smb://{}/{}/{}", connection.getHost(), connection.getBaseDirectory(), targetPath);
        targetFile.write(datadToCopy, 0);
        if (parameters.isRemoveSource()) {
            LOGGER.info("Removing file smb://{}/{}/{}", connection.getHost(), connection.getBaseDirectory(), sourcePath);
            diskShare.rm(sourcePath);
        }
    }

    @Override
    public void rename(SmbConnection connection, FileRenameParameters parameters) throws IOException {
        DiskShare diskShare = connection.getDiskShare();
        String sourcePath = Utility.fixPath(parameters.getSourcePath());

        //always use the source parent directories for the new name
        StringBuffer newName = new StringBuffer(Utility.listFileParentDirs(sourcePath).stream().collect(Collectors.joining("/")));
        newName.append('\\');
        newName.append(Utility.getFilename(parameters.getNewName()));

        LOGGER.info("Renaming file smb://{}/{}/{} to {}", connection.getHost(), connection.getBaseDirectory(), sourcePath, newName);
        SMB2CreateDisposition createDisposition = parameters.isOverwrite() ? SMB2CreateDisposition.FILE_OPEN : SMB2CreateDisposition.FILE_OPEN;

        File renameFile = diskShare.openFile(sourcePath, EnumSet.of(AccessMask.DELETE, AccessMask.GENERIC_WRITE), null, SMB2ShareAccess.ALL, createDisposition, EnumSet.of(SMB2CreateOptions.FILE_NON_DIRECTORY_FILE));
        renameFile.rename(newName.toString(),parameters.isOverwrite());

    }

    @Override
    public void delete(SmbConnection connection, FileDeleteParameters parameters) throws IOException {
        DiskShare diskShare = connection.getDiskShare();
        String targetPath = Utility.fixPath(parameters.getTargetPath());
        if (diskShare.getFileInformation(targetPath).getStandardInformation().isDirectory()) {
            diskShare.rmdir(targetPath, parameters.isRecursive());
        } else {
            diskShare.rm(targetPath);
        }

        LOGGER.info("Deleting smb://{}/{}/{}", connection.getHost(), connection.getBaseDirectory(), targetPath);

    }


    @Override
    public List<FileIdBothDirectoryInformation> listFiles(SmbConnection connection, String path) throws IOException {
        DiskShare diskShare = connection.getDiskShare();
        if (diskShare.getFileInformation(path).getStandardInformation().isDirectory()) {
            return connection.getDiskShare().list(path);
        }
        path = Utility.listFileParentDirs(path).stream().collect(Collectors.joining("/"));
        return connection.getDiskShare().list(path);
    }

    @Override
    public void createDirectory(SmbConnection connection, CreateDirectoryParameters parameters) throws IOException {
        //extract directories
        DiskShare diskShare = connection.getDiskShare();
        String targetPath = Utility.fixPath(parameters.getTargetPath());
        List<String> parentDirs = Utility.listFileParentDirs(targetPath);

        if (parentDirs.isEmpty()) {
            //do nothing if no parent directory
            return;
        }
        LOGGER.info("Creating directory {} at smb://{}/{}", targetPath, connection.getHost(), connection.getBaseDirectory());

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
