package com.wsl.smbConnector.internal.api.payload;

import org.mule.runtime.extension.api.annotation.param.Parameter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TimeInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Parameter
    private final String creationTime;
    @Parameter
    private final String lastAccessTime;
    @Parameter
    private final String lastWriteTime;
    @Parameter
    private final String changeTime;

    public TimeInfo() {
        this("", "", "", "");
    }

    public TimeInfo(String creationTime, String lastAccessTime, String lastWriteTime, String changeTime) {
        this.creationTime = creationTime;
        this.lastAccessTime = lastAccessTime;
        this.lastWriteTime = lastWriteTime;
        this.changeTime = changeTime;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String getLastAccessTime() {
        return lastAccessTime;
    }

    public String getLastWriteTime() {
        return lastWriteTime;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public String toString() {
        return getHashMap().toString();
    }

    public Map<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("creationTime", creationTime);
        map.put("lastAccessTime", lastAccessTime);
        map.put("lastWriteTime", lastWriteTime);
        map.put("changeTime", changeTime);
        return map;
    }
}
