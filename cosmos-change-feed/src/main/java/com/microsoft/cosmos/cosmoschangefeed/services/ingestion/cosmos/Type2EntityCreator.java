package com.microsoft.cosmos.cosmoschangefeed.services.ingestion.cosmos;

import com.microsoft.cosmos.cosmoschangefeed.model.Item;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.model.Type2Item;
import com.microsoft.cosmos.cosmoschangefeed.persistence.entities.Type2Entity;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Component;

@Component
public class Type2EntityCreator implements IItemEntityCreator<Type2Item, Type2Entity> {

  @Override
  public Type2Entity create(Job job, OffsetDateTime timestamp, Item item) {
    return new Type2Entity(job, timestamp, (Type2Item) item);
  }
  
}
