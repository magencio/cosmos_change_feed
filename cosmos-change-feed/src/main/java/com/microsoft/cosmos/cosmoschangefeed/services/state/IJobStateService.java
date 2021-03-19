package com.microsoft.cosmos.cosmoschangefeed.services.state;

import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.model.JobState;

/**
 * Service to update state of a job.
 */
public interface IJobStateService {

  /**
   * Update the state of a job.
   * @param job to update
   * @param state of the job
   * @param failedIngestions number of failed ingestions
   * @param errorMessage in case of error
   */
  void updateState(Job job, JobState state, Integer failedIngestions, String errorMessage);

}
