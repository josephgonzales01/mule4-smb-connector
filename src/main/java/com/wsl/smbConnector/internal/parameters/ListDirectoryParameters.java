package com.wsl.smbConnector.internal.parameters;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

public class ListDirectoryParameters implements SMBFileParameters {

  @Parameter
  @DisplayName("Target Path")
  @Summary("Path to the directory to list, by default it will list base directory")
  @Optional(defaultValue = "")
  private String path;

  @Parameter
  @Summary("Directory content type to list")
  @DisplayName("Content type")
  private ListDirectoryMode listMode;

  @Parameter
  @DisplayName("Search Pattern")
  @Summary("filename pattern to search")
  @Optional(defaultValue = "")
  private String searchPattern;


  public ListDirectoryParameters() {
    this("", "", ListDirectoryMode.ALL);
  }

  public ListDirectoryParameters(String targetPath, String pattern, ListDirectoryMode listMode) {
    this.path = targetPath;
    this.searchPattern = pattern;
    this.listMode = listMode;
  }

  public ListDirectoryMode getListMode() {
    return listMode;
  }

  public String getSearchPattern() {
    return searchPattern.trim();
  }

  public void setListMode(ListDirectoryMode listMode) {
    this.listMode = listMode;
  }

  public void setSearchPattern(String searchPattern) {
    this.searchPattern = searchPattern;
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
