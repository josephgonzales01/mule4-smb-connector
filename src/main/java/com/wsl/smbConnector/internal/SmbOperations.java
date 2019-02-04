package com.wsl.smbConnector.internal;

import com.wsl.smbConnector.internal.api.payload.SmbFileAttributes;
import com.wsl.smbConnector.internal.parameters.*;
import com.wsl.smbConnector.internal.service.SMBOperationService;
import com.wsl.smbConnector.internal.service.SMBOperationServiceImpl;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.dsl.xml.ParameterDsl;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;


/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class SmbOperations {

    private final Logger LOGGER = LoggerFactory.getLogger(SmbOperations.class);

    private final SMBOperationService operationService = new SMBOperationServiceImpl();

    /**
     * Example of an operation that uses the configuration and a connection instance to perform some action.
     */
    @MediaType(value = ANY, strict = false)
    public byte[] read(@Config SmbConfiguration configuration, @Connection SmbConnection connection, @Expression(ExpressionSupport.NOT_SUPPORTED) @ParameterDsl(allowReferences = false) FileReadParameters readParameters) throws IOException {
        LOGGER.info("Reader operation was called");

        return operationService.read(connection, readParameters);
    }

    @Summary("Writes the given \"Content\" in the file pointed by \"Path\"")
    public void write(@Config SmbConfiguration configuration, @Connection SmbConnection connection, @Expression(ExpressionSupport.NOT_SUPPORTED) @ParameterDsl(allowReferences = false) FileWriteParameters writeParameters) throws IOException {
        LOGGER.info("Writer operation was called");
        operationService.write(connection, writeParameters);

    }


    @DisplayName("Move/Copy")
    @Summary("Move or copy a file")
    public void move(@Config SmbConfiguration configuration, @Connection SmbConnection connection, @Expression(ExpressionSupport.NOT_SUPPORTED) @ParameterDsl(allowReferences = false) FileMoveParameters moveParameters) throws IOException {
        LOGGER.info("Move/copy operation was called");
        operationService.move(connection, moveParameters);
    }

    @Summary("Renames a file")
    public void rename(@Config SmbConfiguration configuration, @Connection SmbConnection connection, @Expression(ExpressionSupport.NOT_SUPPORTED) @ParameterDsl(allowReferences = false) FileRenameParameters renameParameters) throws IOException {
        LOGGER.info("Rename operation was called");
        operationService.rename(connection, renameParameters);
    }

    @Summary("Deletes a file")
    public void delete(@Config SmbConfiguration configuration, @Connection SmbConnection connection, @Expression(ExpressionSupport.NOT_SUPPORTED) @ParameterDsl(allowReferences = false) FileDeleteParameters deleteParameters) throws IOException {
        LOGGER.info("Move operation was called");
        operationService.delete(connection, deleteParameters);
    }


    @Summary("List all the files from given directory")
    public List<Result<Map<String, Object>, SmbFileAttributes>> list(@Config SmbConfiguration configuration, @Connection SmbConnection connection, @Expression(ExpressionSupport.NOT_SUPPORTED) @ParameterDsl(allowReferences = false) ListDirectoryParameters listParameters) throws IOException {
        LOGGER.info("List operation was called");
        return operationService.list(connection, listParameters);
    }


}
