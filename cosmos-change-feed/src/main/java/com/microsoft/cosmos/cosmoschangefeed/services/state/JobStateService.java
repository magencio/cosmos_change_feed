package com.microsoft.cosmos.cosmoschangefeed.services.state;

import com.azure.cosmos.models.PartitionKey;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.model.JobState;
import com.microsoft.cosmos.cosmoschangefeed.model.JobStateEntry;
import com.microsoft.cosmos.cosmoschangefeed.persistence.entities.JobStateEntity;
import com.microsoft.cosmos.cosmoschangefeed.persistence.repositories.JobStateRepository;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobStateService implements IJobStateService {

  private JobStateRepository repository;

  @Autowired
  public JobStateService(JobStateRepository repository) {
    this.repository = repository;
  }

  @Override
  public void updateState(Job job, JobState state, Integer failedIngestions, String errorMessage) {
    if (job == null) {
      throw new NullPointerException("Job not set");
    }

    JobStateEntry newJobState = JobStateEntry.builder()
        .state(state)
        .failedIngestions(failedIngestions)
        .errorMessage(errorMessage)
        .timestamp(OffsetDateTime.now(Clock.systemUTC()))
        .build();

    JobStateEntity jobStateEntity = repository.findById(job.getId(), new PartitionKey(job.getSourceId())).block();
    if (jobStateEntity == null) {
      jobStateEntity = new JobStateEntity(job, new ArrayList<JobStateEntry>(), null);
    }

    jobStateEntity.getLog().add(newJobState);
    jobStateEntity.setLastLogEntry(newJobState);

    repository.save(jobStateEntity).block();
  }

}
