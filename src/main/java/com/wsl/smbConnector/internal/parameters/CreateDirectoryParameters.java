package com.wsl.smbConnector.internal.parameters;

import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

public class CreateDirectoryParameters {

    @Parameter
    @DisplayName("Directory Path")
    @Summary("Path to directory and subdirectory")
    private String targetPath;


    public CreateDirectoryParameters() {
        this("");
    }

    public CreateDirectoryParameters(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }
}
