package com.wsl.smbConnector.internal.parameters;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

public class FileWriteParameters {

    @Parameter
    @Summary("Complete Path of the file to be written")
    @DisplayName("Target path")
    private String targetPath;

    @Parameter
    @Summary("Content to be written")
    private String content;

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
        this("", "", FileWriteMode.OVERWRITE, true);
    }

    public FileWriteParameters(String targetPath, String content, FileWriteMode writeMode, boolean createDirectory) {
        this.targetPath = targetPath;
        this.content = content;
        this.writeMode = writeMode;
        this.createDirectory = createDirectory;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public FileWriteMode getWriteMode() {
        return writeMode;
    }

    public boolean isCreateDirectory() {
        return createDirectory;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setWriteMode(FileWriteMode writeMode) {
        this.writeMode = writeMode;
    }

    public void setCreateDirectory(boolean createDirectory) {
        this.createDirectory = createDirectory;
    }

}
