package com.wsl.smbConnector.internal.parameters;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

public class FileDeleteParameters {

    @Parameter
    @DisplayName("Path")
    @Summary("Path to the file to be deleted")
    private String targetPath;

    @Parameter
    @Optional(defaultValue = "false")
    @Summary("Set to true to removes all of its subfolders and files ")
    private boolean recursive;


    public FileDeleteParameters() {
        this("");
    }

    public FileDeleteParameters(String targetPath) {
        this(targetPath, false);
    }

    public FileDeleteParameters(String targetPath, boolean isRecursive) {
        this.targetPath = targetPath;
        this.recursive = isRecursive;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public boolean isRecursive() {
        return recursive;
    }
}
