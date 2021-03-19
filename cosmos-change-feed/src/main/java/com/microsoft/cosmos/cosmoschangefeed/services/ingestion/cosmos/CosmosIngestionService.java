package com.microsoft.cosmos.cosmoschangefeed.services.ingestion.cosmos;

import com.azure.spring.data.cosmos.exception.CosmosAccessException;
import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.microsoft.cosmos.cosmoschangefeed.model.Item;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.persistence.entities.ItemEntity;
import com.microsoft.cosmos.cosmoschangefeed.services.ingestion.IIngestionService;
import java.time.OffsetDateTime;
import java.util.Iterator;
import lombok.Getter;

/**
 * Service to ingest data into CosmosDB.
 */
@Getter
public class CosmosIngestionService<I extends Item, E extends ItemEntity<I>> implements IIngestionService {

  protected ReactiveCosmosRepository<E, String> repository;

  protected IItemEntityCreator<I, E> entityCreator;

  /**
   * Creates a new instance of CosmosIngestionService.
   * @param repository to store the data in CosmosDB
   * @param entityCreator to create the entities to store in the repository
   */
  public CosmosIngestionService(ReactiveCosmosRepository<E, String> repository, IItemEntityCreator<I, E> entityCreator) {
    this.repository = repository;
    this.entityCreator = entityCreator;
  }

  @Override
  public int ingest(Job job, OffsetDateTime timestamp, Iterator<? extends Item> items) {
    if (job == null) {
      throw new NullPointerException("Job not set");
    }

    int failedIngestions = 0;
    while (items.hasNext()) {
      Item item = items.next();
      E entity = entityCreator.create(job, timestamp, item);
      try {
        repository.save(entity).block();
      } catch (CosmosAccessException ex) {
        failedIngestions++;
        ex.printStackTrace();
      }
    }

    return failedIngestions;
  }

}
