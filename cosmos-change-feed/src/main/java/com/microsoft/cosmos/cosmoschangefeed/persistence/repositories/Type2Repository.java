package com.microsoft.cosmos.cosmoschangefeed.persistence.repositories;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.microsoft.cosmos.cosmoschangefeed.persistence.entities.Type2Entity;
import org.springframework.stereotype.Repository;

/**
 * A repository to manage entities that store items of type 2.
 */
@Repository
public interface Type2Repository extends ReactiveCosmosRepository<Type2Entity, String> {
}
