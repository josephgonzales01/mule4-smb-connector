package com.wsl.smbConnector.internal;

import com.wsl.smbConnector.internal.util.Utility;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * This class (as it's name implies) provides connection instances and the funcionality to disconnect and validate those
 * connections.
 * <p>
 * All connection related parameters (values required in order to create a connection) must be
 * declared in the connection providers.
 * <p>
 * This particular example is a {@link PoolingConnectionProvider} which declares that connections resolved by this provider
 * will be pooled and reused. There are other implementations like {@link CachedConnectionProvider} which lazily creates and
 * caches connections or simply {@link ConnectionProvider} if you want a new connection each time something requires one.
 */
public class SmbConnectionProvider implements PoolingConnectionProvider<SmbConnection> {

    private final Logger LOGGER = LoggerFactory.getLogger(SmbConnectionProvider.class);

    @DisplayName("Domain")
    @Expression(ExpressionSupport.SUPPORTED)
    @Optional(defaultValue = "")
    @Parameter
    private String domain;

    @DisplayName("Base Directory")
    @Expression(ExpressionSupport.SUPPORTED)
    @Parameter
    private String baseDirectory;

    @Parameter
    private String host;

    @Parameter
    @Summary("Required Username")
    private String username;

    @Parameter
    @Password
    @Summary("Required Password")
    private String password;

    private SmbConnection smbConnection;

    /**
     * A parameter that is not required to be configured by the user.
     */
    @DisplayName("Connection Timeout")
    @Parameter
    @Optional(defaultValue = SmbConnection.DEFAULT_TIMEOUT_STR)
    @Summary("Timeout in milliseconds, if not set it is defaulted to " + SmbConnection.DEFAULT_TIMEOUT_STR)
    private int connectionTimeout;

    @Override
    public SmbConnection connect() throws ConnectionException {
        try {
            smbConnection = new SmbConnection("", domain, Utility.fixPath(baseDirectory), host, username, password, connectionTimeout);
        } catch (IOException e) {
            throw new ConnectionException(e.getMessage());
        }
        LOGGER.info("Connecting to {}:{}", host, username);
        return smbConnection;
    }

    @Override
    public void disconnect(SmbConnection connection) {
        try {
            connection.invalidate();
        } catch (Exception e) {
            LOGGER.error("Error while disconnecting [" + connection.getId() + "]: " + e.getMessage(), e);
        }
    }

    @Override
    public ConnectionValidationResult validate(SmbConnection connection) {
        return connection.validate();
    }


}
