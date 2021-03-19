package com.microsoft.cosmos.cosmoschangefeed.persistence.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.microsoft.cosmos.cosmoschangefeed.configuration.CosmosConfiguration;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.model.Type1Item;
import java.time.OffsetDateTime;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * An entity that stores items of type 1.
 */
@Container(containerName = CosmosConfiguration.TYPE1_CONTAINER, autoCreateContainer = true)
@NoArgsConstructor
@SuperBuilder
public class Type1Entity extends ItemEntity<Type1Item> {

  public Type1Entity(Job job, OffsetDateTime itemTimestamp, Type1Item item) {
    super(job, itemTimestamp, item);
  }

}
