package com.microsoft.cosmos.cosmoschangefeed.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Azure CosmosDB configuration.
 */
@Configuration
@Getter
public class CosmosConfiguration {

  public static final String JOBSTATE_CONTAINER = "state";
  public static final String TYPE1_CONTAINER = "type1";
  public static final String TYPE2_CONTAINER = "type2";

  private String uri;
  private String key;
  private String database;
  private String feedContainer;
  private String leaseContainer;

  /**
   * Initializes CosmosDB configuration.
   * @param uri of the CosmosDB
   * @param key to access the CosmosDB
   * @param database with the container to monitor
   * @param feedContainer to monitor
   * @param leaseContainer for the change feed to store state
   */
  @Autowired
  public CosmosConfiguration(
      @Value("${azure.cosmos.uri}") String uri,
      @Value("${azure.cosmos.key}") String key,
      @Value("${azure.cosmos.database}") String database,
      @Value("${azure.cosmos.feedcontainer}") String feedContainer,
      @Value("${azure.cosmos.leasecontainer}") String leaseContainer) {
    this.uri = uri;
    this.key = key;
    this.database = database; 
    this.feedContainer = feedContainer;
    this.leaseContainer = leaseContainer;
  }

}

