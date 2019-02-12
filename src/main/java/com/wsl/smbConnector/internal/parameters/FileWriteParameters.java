package com.wsl.smbConnector.internal.parameters;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

public class FileWriteParameters implements SMBFileParameters{

    @Parameter
    @Summary("Complete Path of the file to be written")
    @DisplayName("Target path")
    private String path;

    @Parameter
    @Summary("Content to be written")
    @Content
    private InputStream content;

    @Parameter
    @Optional(defaultValue = "OVERWRITE")
    @Summary("Mode to use when an existing file found")
    @DisplayName("Write mode")
    private FileWriteMode writeMode;

    @Parameter
    @Optional(defaultValue = "true")
    @Summary("Non existing directory in the target path will be created")
    @DisplayName("Create Directory")
    private boolean createDirectory;


    public FileWriteParameters() {
        this("", new ByteArrayInputStream(new byte[0]), FileWriteMode.OVERWRITE, true);
    }

    public FileWriteParameters(String targetPath, InputStream content, FileWriteMode writeMode, boolean createDirectory) {
        this.path = targetPath;
        this.content = content;
        this.writeMode = writeMode;
        this.createDirectory = createDirectory;
    }

    public FileWriteMode getWriteMode() {
        return writeMode;
    }

    public boolean isCreateDirectory() {
        return createDirectory;
    }

    public InputStream getContent() {
        return content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }

    public void setWriteMode(FileWriteMode writeMode) {
        this.writeMode = writeMode;
    }

    public void setCreateDirectory(boolean createDirectory) {
        this.createDirectory = createDirectory;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;

    }
}
