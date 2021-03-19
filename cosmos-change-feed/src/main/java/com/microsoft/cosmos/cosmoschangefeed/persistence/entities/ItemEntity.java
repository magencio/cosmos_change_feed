package com.microsoft.cosmos.cosmoschangefeed.persistence.entities;

import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microsoft.cosmos.cosmoschangefeed.mappers.OffsetDateTimeDeserializer;
import com.microsoft.cosmos.cosmoschangefeed.mappers.OffsetDateTimeSerializer;
import com.microsoft.cosmos.cosmoschangefeed.model.Item;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

/**
 * An entity that stores items referred by a job.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class ItemEntity<T extends Item> {
  
  @Id
  private String id;

  @PartitionKey
  private String partitionKey;

  private String jobId;

  @JsonSerialize(using = OffsetDateTimeSerializer.class)
  @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
  private OffsetDateTime itemTimestamp;

  private T item;

  /**
   * Initializes an entity for an item.
   * @param job associated with the item
   * @param itemTimestamp with the timestamp of the item
   * @param item with the data
   */
  public ItemEntity(Job job, OffsetDateTime itemTimestamp, T item) {
    this.id = job.getSourceId() + "-" + item.getId();
    this.partitionKey = job.getSourceId();
    this.jobId = job.getId();
    this.itemTimestamp = itemTimestamp;
    this.item = item;
  }

}
