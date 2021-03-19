package com.microsoft.cosmos.cosmoschangefeed.persistence.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.microsoft.cosmos.cosmoschangefeed.configuration.CosmosConfiguration;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.model.JobStateEntry;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * An entity to keep track of the state of the processing of a job.
 */
@Container(containerName = CosmosConfiguration.JOBSTATE_CONTAINER, autoCreateContainer = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobStateEntity {

  @Id
  private String id;

  @PartitionKey
  private String partitionKey;
  
  private Job job;

  private List<JobStateEntry> log;

  private JobStateEntry lastLogEntry;

  /**
   * Initializes an entity to record job state.
   * @param job associated with the item
   * @param log list of log state entries
   * @param lastLogEntry last log entry entered in the log
   */
  public JobStateEntity(Job job, List<JobStateEntry> log, JobStateEntry lastLogEntry) {
    this.id = job.getId();
    this.partitionKey = job.getSourceId();
    this.job = job;
    this.log = log;
    this.lastLogEntry = lastLogEntry;
  }

}
