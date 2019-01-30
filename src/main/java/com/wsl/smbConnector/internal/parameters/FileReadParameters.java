package com.wsl.smbConnector.internal.parameters;

import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

public class FileReadParameters {

    @Parameter
    @DisplayName("Target Path")
    @Summary("Path of the file to be read")
    private String targetPath;


    public FileReadParameters() {
        this("");
    }

    public FileReadParameters(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }
}
