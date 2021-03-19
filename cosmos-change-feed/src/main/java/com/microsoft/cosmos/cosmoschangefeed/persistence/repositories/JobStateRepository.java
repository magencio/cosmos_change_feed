package com.microsoft.cosmos.cosmoschangefeed.persistence.repositories;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.microsoft.cosmos.cosmoschangefeed.persistence.entities.JobStateEntity;
import org.springframework.stereotype.Repository;

/**
 * A repository to manage the state of the processing of a job.
 */
@Repository
public interface JobStateRepository extends ReactiveCosmosRepository<JobStateEntity, String> {
}