package com.wsl.smbConnector.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import com.wsl.smbConnector.internal.api.payload.SmbFileAttributes;
import com.wsl.smbConnector.internal.parameters.FileDeleteParameters;
import com.wsl.smbConnector.internal.parameters.FileMoveParameters;
import com.wsl.smbConnector.internal.parameters.FileReadParameters;
import com.wsl.smbConnector.internal.parameters.FileRenameParameters;
import com.wsl.smbConnector.internal.parameters.FileWriteParameters;
import com.wsl.smbConnector.internal.parameters.ListDirectoryParameters;
import com.wsl.smbConnector.internal.service.SMBDeleteOperationService;
import com.wsl.smbConnector.internal.service.SMBListOperationService;
import com.wsl.smbConnector.internal.service.SMBMoveOperationService;
import com.wsl.smbConnector.internal.service.SMBReadOperationService;
import com.wsl.smbConnector.internal.service.SMBRenameOperationService;
import com.wsl.smbConnector.internal.service.SMBWriteOperationService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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


/**
 * This class is a container for operations, every public method in this class will be taken as an
 * extension operation.
 */
public class SmbOperations {

  private final Logger LOGGER = LoggerFactory.getLogger(SmbOperations.class);


  private SMBReadOperationService readService;
  private SMBWriteOperationService writeService;
  private SMBRenameOperationService renameService;
  private SMBMoveOperationService moveService;
  private SMBDeleteOperationService deleteService;
  private SMBListOperationService listService;

  public SmbOperations() {
    readService = new SMBReadOperationService();
    writeService = new SMBWriteOperationService();
    renameService = new SMBRenameOperationService();
    moveService = new SMBMoveOperationService();
    deleteService = new SMBDeleteOperationService();
    listService = new SMBListOperationService();
  }

  /**
   * Example of an operation that uses the configuration and a connection instance to perform some
   * action.
   */
  @MediaType(value = ANY, strict = false)
  public Result<byte[], Void> read(@Config SmbConfiguration configuration,
      @Connection SmbConnection connection,
      @Expression(ExpressionSupport.NOT_SUPPORTED) @ParameterDsl(allowReferences = false) FileReadParameters readParameters)
      throws IOException {
    LOGGER.info("Reader operation was called");
    return readService.perform(connection, readParameters);

  }

  @Summary("Writes the given \"Content\" in the file pointed by \"Path\"")
  public void write(@Config SmbConfiguration configuration, @Connection SmbConnection connection,
      @Expression(ExpressionSupport.NOT_SUPPORTED) @ParameterDsl(allowReferences = false) FileWriteParameters writeParameters)
      throws IOException {
    LOGGER.info("Writer operation was called");
    writeService.perform(connection, writeParameters);

  }


  @DisplayName("Move/Copy")
  @Summary("Move or copy a file")
  public void move(@Config SmbConfiguration configuration, @Connection SmbConnection connection,
      @Expression(ExpressionSupport.NOT_SUPPORTED) @ParameterDsl(allowReferences = false) FileMoveParameters moveParameters)
      throws IOException {
    LOGGER.info("Move/copy operation was called");
    moveService.perform(connection, moveParameters);
  }

  @Summary("Renames a file")
  public void rename(@Config SmbConfiguration configuration, @Connection SmbConnection connection,
      @Expression(ExpressionSupport.NOT_SUPPORTED) @ParameterDsl(allowReferences = false) FileRenameParameters renameParameters)
      throws IOException {
    LOGGER.info("Rename operation was called");
    renameService.perform(connection, renameParameters);
  }

  @Summary("Deletes a file")
  public void delete(@Config SmbConfiguration configuration, @Connection SmbConnection connection,
      @Expression(ExpressionSupport.NOT_SUPPORTED) @ParameterDsl(allowReferences = false) FileDeleteParameters deleteParameters)
      throws IOException {
    LOGGER.info("Move operation was called");
    deleteService.perform(connection, deleteParameters);
  }


  @Summary("List all the files from given directory")
  public List<Result<Map<String, Object>, SmbFileAttributes>> list(
      @Config SmbConfiguration configuration, @Connection SmbConnection connection,
      @Expression(ExpressionSupport.NOT_SUPPORTED) @ParameterDsl(allowReferences = false) ListDirectoryParameters listParameters)
      throws IOException {
    LOGGER.info("List operation was called");
    return listService.perform(connection, listParameters);

  }


}
