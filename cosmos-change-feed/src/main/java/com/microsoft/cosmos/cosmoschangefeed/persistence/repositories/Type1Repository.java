package com.microsoft.cosmos.cosmoschangefeed.persistence.repositories;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.microsoft.cosmos.cosmoschangefeed.persistence.entities.Type1Entity;
import org.springframework.stereotype.Repository;

/**
 * A repository to manage entities that store items of type 1.
 */
@Repository
public interface Type1Repository extends ReactiveCosmosRepository<Type1Entity, String> {
}
