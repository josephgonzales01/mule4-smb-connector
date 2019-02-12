package com.wsl.smbConnector.internal.parameters;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

public class FileMoveParameters implements SMBFileParameters {

    @Parameter
    @Summary("Path to the file to be move")
    @DisplayName("Source Path")
    private String path;

    @Parameter
    @Summary("Complete Target path name")
    @DisplayName("Target path")
    private String targetPath;

    @Parameter
    @Summary("Existing file will be overwritten")
    @Optional(defaultValue = "true")
    private boolean overwrite;

    @Parameter
    @Summary("Non existing directory in the path will be created")
    @DisplayName("Create Directory")
    @Optional(defaultValue = "true")
    private boolean createDirectory;

    @Parameter
    @Summary("Remove source file after move")
    @DisplayName("Remove source")
    @Optional(defaultValue = "true")
    private boolean removeSource;

    public FileMoveParameters() {
        this("", "", true, true, true);
    }

    public FileMoveParameters(String sourcePath, String targetPath, boolean overwrite, boolean createDirectory, boolean removeSource) {
        this.path = sourcePath;
        this.targetPath = targetPath;

        this.overwrite = overwrite;
        this.createDirectory = createDirectory;
        this.removeSource = removeSource;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public boolean isCreateDirectory() {
        return createDirectory;
    }

    public boolean isRemoveSource() {
        return removeSource;
    }

    public void setPath(String sourcePath) {
        this.path = sourcePath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public void setCreateDirectory(boolean createDirectory) {
        this.createDirectory = createDirectory;
    }

    public void setRemoveSource(boolean removeSource) {
        this.removeSource = removeSource;
    }

    @Override
    public String getPath() {
        return path;
    }


}
