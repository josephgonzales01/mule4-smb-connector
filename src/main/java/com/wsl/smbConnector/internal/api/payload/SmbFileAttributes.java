package com.wsl.smbConnector.internal.api.payload;

import org.mule.runtime.extension.api.annotation.param.Parameter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SmbFileAttributes implements Serializable {

    //default serialVersion id
    private static final long serialVersionUID = 1L;

    @Parameter
    private final String fileName;
    @Parameter
    private final TimeInfo timeInfo;
    @Parameter
    private final long fileSize;


    public SmbFileAttributes() {
        this("", 0L, new TimeInfo());
    }

    public SmbFileAttributes(String fileName, long fileSize, TimeInfo timeInfo) {
        this.fileName = fileName;
        this.timeInfo = timeInfo;
        this.fileSize = fileSize;
    }

    public TimeInfo getTimeInfo() {
        return timeInfo;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public String toString() {
        return getHashMap().toString();
    }

    public Map<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap();
        map.put("fileName", fileName);
        map.put("fileSize", fileSize);
        map.put("timeInfo", timeInfo.getHashMap());
        return map;
    }


}
