package com.wsl.smbConnector.internal.parameters;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

public class FileDeleteParameters implements SMBFileParameters{

    @Parameter
    @DisplayName("Path")
    @Summary("Path to the file to be deleted")
    private String path;

    @Parameter
    @Optional(defaultValue = "false")
    @Summary("Set to true to removes all of its subfolders and files ")
    private boolean recursive;


    public FileDeleteParameters() {
        this("");
    }

    public FileDeleteParameters(String path) {
        this(path, false);
    }

    public FileDeleteParameters(String path, boolean isRecursive) {
        this.path = path;
        this.recursive = isRecursive;
    }


    public void setPath(String path) {
        this.path = path;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

    @Override
    public String getPath() {
        return path;
    }
}
