package com.microsoft.cosmos.cosmoschangefeed.processors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.cosmos.cosmoschangefeed.exceptions.InvalidJobException;
import com.microsoft.cosmos.cosmoschangefeed.exceptions.ProcessException;
import com.microsoft.cosmos.cosmoschangefeed.model.Item;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.model.JobState;
import com.microsoft.cosmos.cosmoschangefeed.services.datasource.IDataSourceService;
import com.microsoft.cosmos.cosmoschangefeed.services.datasource.IDataSourceServiceFactory;
import com.microsoft.cosmos.cosmoschangefeed.services.ingestion.IIngestionService;
import com.microsoft.cosmos.cosmoschangefeed.services.ingestion.IIngestionServiceFactory;
import com.microsoft.cosmos.cosmoschangefeed.services.state.IJobStateService;
import java.time.OffsetDateTime;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobProcessor implements IProcessor {

  private IJobStateService jobStateService;
  private IDataSourceServiceFactory dataSourceServiceFactory;
  private IIngestionServiceFactory ingestionServiceFactory;

  @Autowired
  public JobProcessor(
      IJobStateService jobStateService,
      IDataSourceServiceFactory dataSourceServiceFactory, 
      IIngestionServiceFactory ingestionServiceFactory) {
    this.jobStateService = jobStateService;
    this.dataSourceServiceFactory = dataSourceServiceFactory;
    this.ingestionServiceFactory = ingestionServiceFactory;
  }

  @Override
  public void process(JsonNode document) {
    Job job = null;

    try {
      // Get job
      job = toJob(document);
      jobStateService.updateState(job, JobState.PROCESSING, -1, null);

      // Get timestamp of the data associated to the job
      IDataSourceService dataSourceService = getDataSourceService(job);
      OffsetDateTime timestamp = dataSourceService.getTimestamp(job);

      // Get items included in that data
      Iterator<? extends Item> items = dataSourceService.getItems(job);

      // Ingest the items in the database
      IIngestionService ingestionService = getIngestionService(job);
      int failedIngestions = ingestionService.ingest(job, timestamp, items);

      jobStateService.updateState(job, JobState.COMPLETED, failedIngestions, null);

    } catch (InvalidJobException ex) {
      // No job, so we cannot update its state
      throw new ProcessException("Invalid job", ex);
    } catch (Exception ex) {
      String errorMessage = "Unexpected error";
      jobStateService.updateState(job, JobState.ERROR, -1, errorMessage);
      throw new ProcessException(errorMessage, ex);
    }
  }

  private Job toJob(JsonNode document) {
    try {
      return new ObjectMapper().convertValue(document, Job.class);
    } catch (Exception ex) {
      throw new InvalidJobException("Failed to read job", ex);
    }
  }

  private IDataSourceService getDataSourceService(Job job) {
    return dataSourceServiceFactory.getService(job.getDataType() + "DataSourceService");
  }

  private IIngestionService getIngestionService(Job job) {
    return ingestionServiceFactory.getService(job.getDataType() + "IngestionService");
  }

}
