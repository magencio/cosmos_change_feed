package com.microsoft.cosmos.cosmoschangefeed.services.ingestion;

import com.microsoft.cosmos.cosmoschangefeed.model.Item;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import java.time.OffsetDateTime;
import java.util.Iterator;

/**
 * Service to ingest data into a database/service.
 */
public interface IIngestionService {

  /**
   * Ingest data.
   * @param job associated to the data
   * @param timestamp of the data
   * @param items contained in the data
   * @return number of items that couldn't be ingested
   */
  int ingest(Job job, OffsetDateTime timestamp, Iterator<? extends Item> items);

}

