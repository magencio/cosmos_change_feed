package com.microsoft.cosmos.cosmoschangefeed.processors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microsoft.cosmos.cosmoschangefeed.exceptions.InvalidJobException;
import com.microsoft.cosmos.cosmoschangefeed.exceptions.ProcessException;
import com.microsoft.cosmos.cosmoschangefeed.model.DataType;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.model.JobState;
import com.microsoft.cosmos.cosmoschangefeed.model.Type1Item;
import com.microsoft.cosmos.cosmoschangefeed.services.datasource.IDataSourceService;
import com.microsoft.cosmos.cosmoschangefeed.services.datasource.IDataSourceServiceFactory;
import com.microsoft.cosmos.cosmoschangefeed.services.ingestion.IIngestionService;
import com.microsoft.cosmos.cosmoschangefeed.services.ingestion.IIngestionServiceFactory;
import com.microsoft.cosmos.cosmoschangefeed.services.state.IJobStateService;
import com.microsoft.cosmos.cosmoschangefeed.testutils.TestData;
import com.microsoft.cosmos.cosmoschangefeed.testutils.TestMapper;
import com.microsoft.cosmos.cosmoschangefeed.testutils.TestUtils;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JobProcessorTest {

  ObjectMapper mapper;

  @Mock
  IJobStateService jobStateService;

  @Mock
  IDataSourceServiceFactory dataSourceServiceFactory;

  @Mock
  IDataSourceService dataSourceService;

  @Mock
  IIngestionServiceFactory ingestionServiceFactory;

  @Mock
  IIngestionService ingestionService;

  @Captor
  ArgumentCaptor<Job> jobCaptor;

  @Captor
  ArgumentCaptor<OffsetDateTime> timestampCaptor;

  @Captor
  ArgumentCaptor<Iterator<Type1Item>> type1ItemsCaptor;

  @Captor
  ArgumentCaptor<JobState> jobStateCaptor;
  
  @Captor
  ArgumentCaptor<Integer> failedIngestionsCaptor;
  
  @Captor
  ArgumentCaptor<String> errorMessageCaptor;

  /**
   * Run before every test.
   */
  @Before
  public void init() {
    // Mapper
    this.mapper = TestMapper.getSerializerMapper();

    // Factories
    doReturn(dataSourceService).when(dataSourceServiceFactory).getService("TYPE1DataSourceService");
    doReturn(ingestionService).when(ingestionServiceFactory).getService("TYPE1IngestionService");
  }

  @Test
  public void process_whenJobIsValid_thenItemsAreInserted() {
    // Arrange
    final Job job = TestData.getValidJob(DataType.TYPE1);

    final OffsetDateTime timestamp = TestData.getValidTimestamp();
    when(dataSourceService.getTimestamp(any())).thenReturn(timestamp);

    final List<Type1Item> items = TestData.getValidType1Items();
    doReturn(items.iterator()).when(dataSourceService).getItems(any());

    doReturn(1).when(ingestionService).ingest(any(), any(), any());

    JsonNode document = mapper.valueToTree(job);

    JobProcessor processor = new JobProcessor(jobStateService, dataSourceServiceFactory, ingestionServiceFactory);

    // Act
    processor.process(document);

    // Assert
    verify(jobStateService, times(2))
        .updateState(jobCaptor.capture(), jobStateCaptor.capture(), failedIngestionsCaptor.capture(), errorMessageCaptor.capture());
    List<Job> capturedJobs = jobCaptor.getAllValues();
    assertThat(capturedJobs).usingRecursiveComparison().isEqualTo(Arrays.asList(job, job));
    List<JobState> capturedJobStates = jobStateCaptor.getAllValues();
    assertThat(capturedJobStates).usingRecursiveComparison().isEqualTo(Arrays.asList(JobState.PROCESSING, JobState.COMPLETED));
    List<Integer> capturedFailedIngestions = failedIngestionsCaptor.getAllValues();
    assertThat(capturedFailedIngestions).usingRecursiveComparison().isEqualTo(Arrays.asList(-1, 1));
    List<String> capturedErrorMessages = errorMessageCaptor.getAllValues();
    assertThat(capturedErrorMessages).usingRecursiveComparison().isEqualTo(Arrays.asList(null, null));
    
    verify(dataSourceService, times(1)).getTimestamp(jobCaptor.capture());
    Job capturedJob = jobCaptor.getValue();
    assertThat(capturedJob).usingRecursiveComparison().isEqualTo(job);

    verify(dataSourceService, times(1)).getItems(jobCaptor.capture());
    capturedJob = jobCaptor.getValue();
    assertThat(capturedJob).usingRecursiveComparison().isEqualTo(job);

    verify(ingestionService, times(1)).ingest(jobCaptor.capture(), timestampCaptor.capture(), type1ItemsCaptor.capture());
    capturedJob = jobCaptor.getValue();
    assertThat(capturedJob).usingRecursiveComparison().isEqualTo(job);
    OffsetDateTime capturedTimestamp = timestampCaptor.getValue();
    assertThat(capturedTimestamp).isEqualTo(timestamp);
    List<Type1Item> capturedItems = TestUtils.toList(type1ItemsCaptor.getValue());
    assertThat(capturedItems).usingRecursiveComparison().isEqualTo(items);
  }

  @Test
  public void process_whenJobMissesMandatoryProperty_thenExceptionIsThrownAndNoItemsAreInserted() {
    // Arrange
    JobProcessor processor = new JobProcessor(jobStateService, dataSourceServiceFactory, ingestionServiceFactory);
    Job job = TestData.getValidJob(DataType.TYPE1);
    JsonNode document = mapper.valueToTree(job);
    ((ObjectNode) document).remove("dataPath");

    // Act & Assert
    assertThatThrownBy(() -> {
      processor.process(document); 
    })
        .isInstanceOf(ProcessException.class)
        .hasCauseInstanceOf(InvalidJobException.class);

    verify(jobStateService, never()).updateState(any(), any(), any(), any());

    verify(ingestionService, never()).ingest(any(), any(), any());
  }

}
