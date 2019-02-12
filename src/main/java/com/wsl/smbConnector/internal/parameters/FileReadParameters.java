package com.wsl.smbConnector.internal.parameters;

import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

public class FileReadParameters implements SMBFileParameters{

    @Parameter
    @DisplayName("Target Path")
    @Summary("Path of the file to be read")
    private String path;


    public FileReadParameters() {
        this("");
    }

    public FileReadParameters(String targetPath) {
        this.path = targetPath;
    }

    public void setPath(String targetPath) {
        this.path = targetPath;
    }

    @Override
    public String getPath() {
        return path;
    }
}
