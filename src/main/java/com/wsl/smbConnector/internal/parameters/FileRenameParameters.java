package com.wsl.smbConnector.internal.parameters;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

public class FileRenameParameters implements SMBFileParameters{

    @Parameter
    @Summary("Path to the file to be renamed")
    @DisplayName("Source Path")
    private String path;

    @Parameter
    @Summary("New file name, parent directories will be disregarded")
    @DisplayName("New name")
    private String newName;


    @Parameter
    @Summary("Existing file with the same name will be overwritten")
    @Optional(defaultValue = "true")
    private boolean overwrite;


    public FileRenameParameters() {
        this("", "", true);
    }

    public FileRenameParameters(String sourcePath, String newName, boolean overwrite) {
        this.path = sourcePath;
        this.newName = newName;
        this.overwrite = overwrite;
    }

    @Override
    public String getPath() {
        return path;
    }

    public String getNewName() {
        return newName;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

}
