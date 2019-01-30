package com.wsl.smbConnector.internal;


import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import org.mule.runtime.api.connection.ConnectionValidationResult;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class SmbConnection {


    private final String id;

    private final String domain;

    private final String baseDirectory;

    private final String host;

    private final String username;

    private final String password;

    private final int timeout;

    public static final String DEFAULT_TIMEOUT_STR = "10000";
    /* 192.168.1.2/smb_shared */

    public static final int DEFAULT_TIMEOUT = 10000;

    private DiskShare diskShare;

    public SmbConnection() throws IOException {
        this("", "", "", "", "", "", DEFAULT_TIMEOUT);
    }

    public SmbConnection(String id, String domain, String baseDirectory, String host, String username, String password, int timeout) throws IOException {
        this.id = id;
        this.domain = domain;
        this.baseDirectory = baseDirectory;
        this.host = host;
        this.username = username;
        this.password = password;
        this.timeout = timeout;

        initConnection();
    }

    private DiskShare initConnection() throws IOException {
        SmbConfig config = SmbConfig.builder().withTimeout(timeout, TimeUnit.MILLISECONDS).build();
        SMBClient smbClient = new SMBClient(config);

        Connection connection = smbClient.connect(host);
        Session session = connection.authenticate(new AuthenticationContext(username, password.toCharArray(), domain));
        diskShare = (DiskShare) session.connectShare(baseDirectory);
        return diskShare;
    }

    public String getBaseDirectory() {
        return baseDirectory;
    }

    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public void invalidate() throws IOException {

        if (diskShare != null)
            diskShare.close();
    }

    public ConnectionValidationResult validate() {
        return ConnectionValidationResult.success();
    }


    public int getTimeout() {
        return timeout;
    }

    public String getId() {
        return id;
    }

    public String getDomain() {
        return domain;
    }

    public DiskShare getDiskShare() throws IOException {
        if (diskShare == null) {
            return initConnection();
        }
        return diskShare;
    }

}
