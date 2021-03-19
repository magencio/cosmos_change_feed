package com.microsoft.cosmos.cosmoschangefeed.persistence.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.microsoft.cosmos.cosmoschangefeed.configuration.CosmosConfiguration;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.model.Type2Item;
import java.time.OffsetDateTime;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * An entity that stores items of type 2.
 */
@Container(containerName = CosmosConfiguration.TYPE2_CONTAINER, autoCreateContainer = true)
@NoArgsConstructor
@SuperBuilder
public class Type2Entity extends ItemEntity<Type2Item> {

  public Type2Entity(Job job, OffsetDateTime itemTimestamp, Type2Item item) {
    super(job, itemTimestamp, item);
  }

}
