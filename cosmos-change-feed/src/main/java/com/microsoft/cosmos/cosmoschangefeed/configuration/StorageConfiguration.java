package com.microsoft.cosmos.cosmoschangefeed.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Azure DataLake Storage Gen2 configuration.
 */
@Configuration
@Getter
public class StorageConfiguration {

  private String accountName;
  private String connectionString;

  /**
   * Initializes Storage configuration.
   * @param accountName name of the Storage account
   * @param connectionString connection string to Storage
   */
  @Autowired
  public StorageConfiguration(
      @Value("${azure.storage.accountname}") String accountName,
      @Value("${azure.storage.connectionstring}") String connectionString) {
    this.accountName = accountName;
    this.connectionString = connectionString;
  }

}
