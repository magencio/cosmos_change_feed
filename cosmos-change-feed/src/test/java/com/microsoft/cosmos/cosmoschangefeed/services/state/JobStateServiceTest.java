package com.microsoft.cosmos.cosmoschangefeed.services.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.azure.cosmos.models.PartitionKey;
import com.azure.spring.data.cosmos.exception.CosmosAccessException;
import com.microsoft.cosmos.cosmoschangefeed.model.DataType;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.model.JobState;
import com.microsoft.cosmos.cosmoschangefeed.model.JobStateEntry;
import com.microsoft.cosmos.cosmoschangefeed.persistence.entities.JobStateEntity;
import com.microsoft.cosmos.cosmoschangefeed.persistence.repositories.JobStateRepository;
import com.microsoft.cosmos.cosmoschangefeed.testutils.TestData;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

@RunWith(MockitoJUnitRunner.class)
public class JobStateServiceTest {

  Comparator<JobStateEntry> jobStateEntryComparator = (jst1, jst2) ->
      jst1.getState() == jst2.getState()
      && jst1.getFailedIngestions() == jst2.getFailedIngestions()
      && jst1.getErrorMessage() == jst2.getErrorMessage() ? 0 : 1;

  @Mock
  JobStateRepository repository;

  @Captor
  ArgumentCaptor<String> idCaptor;

  @Captor
  ArgumentCaptor<PartitionKey> partitionKeyCaptor;

  @Captor
  ArgumentCaptor<JobStateEntity> jobStateEntityCaptor;

  @Test
  public void updateState_whenValidDataIsProvidedAndNoPreviousStateExists_thenNewStateIsCreated() {
    // Arrange
    Job job = TestData.getValidJob(DataType.TYPE1);
    JobState state = JobState.COMPLETED;
    int failedIngestions = 2;
    String errorMessage = "some message";

    final JobStateEntry stateEntry = createJobStateEntry(state, failedIngestions, errorMessage);
    final JobStateEntity entity =
        createJobStateEntity(job, new ArrayList<JobStateEntry>(Arrays.asList(stateEntry)), stateEntry);

    when(repository.findById(anyString(), any(PartitionKey.class))).thenReturn(Mono.empty());

    when(repository.save(any())).thenAnswer(i -> Mono.just(i.getArgument(0)));

    JobStateService service = new JobStateService(repository);

    // Act
    service.updateState(job, state, failedIngestions, errorMessage);

    // Assert
    verify(repository, times(1)).save(jobStateEntityCaptor.capture());
    JobStateEntity capturedEntity = jobStateEntityCaptor.getValue();
    assertThat(capturedEntity).usingRecursiveComparison()
        .withComparatorForType(jobStateEntryComparator, JobStateEntry.class).isEqualTo(entity);
  }

  @Test
  public void updateState_whenValidDataIsProvidedAndPreviousStateExists_thenStateIsUpdated() {
    // Arrange
    Job job = TestData.getValidJob(DataType.TYPE1);
    JobState state = JobState.COMPLETED;
    int failedIngestions = 2;
    String errorMessage = "some message";

    final JobStateEntry stateEntry = createJobStateEntry(JobState.PROCESSING, -1, null);
    final JobStateEntity entity =
        createJobStateEntity(job, new ArrayList<JobStateEntry>(Arrays.asList(stateEntry)), stateEntry);

    when(repository.findById(anyString(), any(PartitionKey.class))).thenReturn(Mono.just(entity));

    when(repository.save(any())).thenAnswer(i -> Mono.just(i.getArgument(0)));

    final JobStateEntry newStateEntry = createJobStateEntry(state, failedIngestions, errorMessage);
    final JobStateEntity expectedEntity =
        createJobStateEntity(job, new ArrayList<JobStateEntry>(Arrays.asList(stateEntry, newStateEntry)), newStateEntry);

    JobStateService service = new JobStateService(repository);

    // Act
    service.updateState(job, state, failedIngestions, errorMessage);

    // Assert
    verify(repository, times(1)).findById(idCaptor.capture(), partitionKeyCaptor.capture());
    String capturedId = idCaptor.getValue();
    assertThat(capturedId).isEqualTo(job.getId());
    PartitionKey capturedPartitionKey = partitionKeyCaptor.getValue();
    assertThat(capturedPartitionKey).usingRecursiveComparison().isEqualTo(new PartitionKey(job.getSourceId()));

    verify(repository, times(1)).save(jobStateEntityCaptor.capture());
    JobStateEntity capturedEntity = jobStateEntityCaptor.getValue();
    assertThat(capturedEntity).usingRecursiveComparison()
        .withComparatorForType(jobStateEntryComparator, JobStateEntry.class).isEqualTo(expectedEntity);
  }

  @Test
  public void updateState_whenNoJobIsProvided_thenExceptionIsThrown() {
    // Arrange
    JobStateService service = new JobStateService(repository);

    // Act & Assert
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> {
          service.updateState(null, JobState.PROCESSING, -1, null);
        });
  }

  @Test
  public void updateState_whenRepositoryFailsToFindEntity_thenExceptionIsThrown() {
    // Arrange
    Job job = TestData.getValidJob(DataType.TYPE1);
    JobState state = JobState.COMPLETED;
    int failedIngestions = 2;
    String errorMessage = "some message";

    when(repository.findById(any(String.class), any(PartitionKey.class))).thenThrow(CosmosAccessException.class);

    JobStateService service = new JobStateService(repository);

    // Act & Assert
    assertThatExceptionOfType(CosmosAccessException.class)
        .isThrownBy(() -> {
          service.updateState(job, state, failedIngestions, errorMessage);
        });
  }

  @Test
  public void updateState_whenRepositoryFailsToSaveEntity_thenExceptionIsThrown() {
    // Arrange
    Job job = TestData.getValidJob(DataType.TYPE1);
    JobState state = JobState.COMPLETED;
    int failedIngestions = 2;
    String errorMessage = "some message";

    when(repository.findById(any(String.class), any(PartitionKey.class))).thenReturn(Mono.empty());

    when(repository.save(any(JobStateEntity.class))).thenThrow(CosmosAccessException.class);
    
    JobStateService service = new JobStateService(repository);

    // Act & Assert
    assertThatExceptionOfType(CosmosAccessException.class)
        .isThrownBy(() -> {
          service.updateState(job, state, failedIngestions, errorMessage);
        });
  }

  private JobStateEntry createJobStateEntry(JobState state, int failedIngestions, String errorMessage) {
    return JobStateEntry.builder()
        .state(state)
        .failedIngestions(failedIngestions)
        .errorMessage(errorMessage)
        .timestamp(OffsetDateTime.now(Clock.systemUTC()))
        .build();
  }

  private JobStateEntity createJobStateEntity(Job job, List<JobStateEntry> log, JobStateEntry lastLogEntry) {
    return JobStateEntity.builder()
        .id(job.getId())
        .partitionKey(job.getSourceId())
        .job(job)
        .log(log)
        .lastLogEntry(lastLogEntry)
        .build();
  }

}
