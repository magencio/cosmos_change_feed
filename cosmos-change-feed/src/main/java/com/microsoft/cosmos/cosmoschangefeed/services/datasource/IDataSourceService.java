package com.microsoft.cosmos.cosmoschangefeed.services.datasource;

import com.microsoft.cosmos.cosmoschangefeed.model.Item;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import java.time.OffsetDateTime;
import java.util.Iterator;

/**
 * Service to access job related data from a data source.
 */
public interface IDataSourceService {

  /**
   * Get timestamp of the data associated to a job.
   * @param job which timestamp we want
   * @return timestamp
   */
  OffsetDateTime getTimestamp(Job job);
  
  /**
   * Get items contained in the data associated to a job.
   * @param job which items we want
   * @return items
   */
  Iterator<? extends Item> getItems(Job job);

}
