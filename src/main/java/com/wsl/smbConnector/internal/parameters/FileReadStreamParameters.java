package com.wsl.smbConnector.internal.parameters;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

public class FileReadStreamParameters implements SMBFileParameters{

    @Parameter
    @DisplayName("Target Path")
    @Summary("Path of the file to be read")
    private String path;

    @Parameter
    @DisplayName("Chunk Size")
    @Summary("The maximum number of bytes to read")
    private long chunkSize;

    @Parameter
    @DisplayName("Offset Size")
    @Optional(defaultValue = "0")
    @Summary("The offset, in bytes, into the file from which the data should be read")
    private long offset;


    public FileReadStreamParameters() {
        this("",1024,0L);
    }

    public FileReadStreamParameters(String targetPath,long chunkSize, long offset) {
        this.path = targetPath;
        this.chunkSize = chunkSize;
        this.offset = offset;
    }

    public void setPath(String targetPath) {
        this.path = targetPath;
    }

    @Override
    public String getPath() {
        return path;
    }

    public long getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(long chunkSize) {
        this.chunkSize = chunkSize;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}
