package com.microsoft.cosmos.cosmoschangefeed.services.ingestion.cosmos;

import com.microsoft.cosmos.cosmoschangefeed.model.Item;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.persistence.entities.ItemEntity;
import java.time.OffsetDateTime;

/**
 * To create entities.
 */
public interface IItemEntityCreator<I extends Item, E extends ItemEntity<I>> {

  /**
   * Create an entity to store one item. 
   * @param job associated to the item
   * @param timestamp of the job
   * @param item that will be associated to the entity
   * @return entity
   */
  E create(Job job, OffsetDateTime timestamp, Item item);

}
