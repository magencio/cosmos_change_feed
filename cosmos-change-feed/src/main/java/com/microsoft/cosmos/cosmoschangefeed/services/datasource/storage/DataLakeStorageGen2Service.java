package com.microsoft.cosmos.cosmoschangefeed.services.datasource.storage;

import com.azure.storage.common.StorageSharedKeyCredential;
import com.azure.storage.file.datalake.DataLakeServiceClient;
import com.azure.storage.file.datalake.DataLakeServiceClientBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.cosmos.cosmoschangefeed.configuration.StorageConfiguration;
import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to access data from Azure Data Lake Storage Gen2.
 */
@Service
public class DataLakeStorageGen2Service implements IStorageService {

  private final DataLakeServiceClient serviceClient;
  
  /**
   * Initializes the Storage service.
   * @param configuration to access the Storage.
   */
  @Autowired
  public DataLakeStorageGen2Service(StorageConfiguration configuration) {
    StorageSharedKeyCredential credential = StorageSharedKeyCredential
        .fromConnectionString(configuration.getConnectionString());
    this.serviceClient = new DataLakeServiceClientBuilder()
        .credential(credential)
        .endpoint("https://" + configuration.getAccountName() + ".dfs.core.windows.net")
        .buildClient();
  }

  @Override
  public JsonNode downloadJson(String container, String path) {
    JsonNode json = null;
    
    try {
      Path fullPath = Paths.get(path);
      String directory = fullPath.getParent().toString();
      String file = fullPath.getFileName().toString();
      
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      serviceClient.getFileSystemClient(container)
          .getDirectoryClient(directory)
          .getFileClient(file)
          .read(output);
      json = new ObjectMapper().readTree(output.toString());

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return json;
  }

}
