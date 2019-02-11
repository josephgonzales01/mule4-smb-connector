package com.wsl.smbConnector.internal.parameters;

import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

public class CreateDirectoryParameters implements SMBFileParameters {

  @Parameter
  @DisplayName("Directory Path")
  @Summary("Path to directory and subdirectory")
  private String path;


  public CreateDirectoryParameters() {
    this("");
  }

  public CreateDirectoryParameters(String targetPath) {
    this.path = targetPath;
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
