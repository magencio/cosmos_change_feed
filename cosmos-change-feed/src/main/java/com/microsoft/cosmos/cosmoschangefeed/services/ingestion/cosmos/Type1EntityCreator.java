package com.microsoft.cosmos.cosmoschangefeed.services.ingestion.cosmos;

import com.microsoft.cosmos.cosmoschangefeed.model.Item;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.model.Type1Item;
import com.microsoft.cosmos.cosmoschangefeed.persistence.entities.Type1Entity;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Component;

@Component
public class Type1EntityCreator implements IItemEntityCreator<Type1Item, Type1Entity> {

  @Override
  public Type1Entity create(Job job, OffsetDateTime timestamp, Item item) {
    return new Type1Entity(job, timestamp, (Type1Item) item);
  }
  
}
