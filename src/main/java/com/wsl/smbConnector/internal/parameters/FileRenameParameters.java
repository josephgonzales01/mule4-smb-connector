package com.wsl.smbConnector.internal.parameters;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

public class FileRenameParameters {

    @Parameter
    @Summary("Path to the file to be renamed")
    @DisplayName("Source Path")
    private String sourcePath;

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
        this.sourcePath = sourcePath;
        this.newName = newName;
        this.overwrite = overwrite;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public String getNewName() {
        return newName;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
