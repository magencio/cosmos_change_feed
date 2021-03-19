package com.microsoft.cosmos.cosmoschangefeed.services.ingestion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.azure.spring.data.cosmos.exception.CosmosAccessException;
import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.microsoft.cosmos.cosmoschangefeed.model.DataType;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.model.Type1Item;
import com.microsoft.cosmos.cosmoschangefeed.persistence.entities.Type1Entity;
import com.microsoft.cosmos.cosmoschangefeed.services.ingestion.cosmos.CosmosIngestionService;
import com.microsoft.cosmos.cosmoschangefeed.services.ingestion.cosmos.IItemEntityCreator;
import com.microsoft.cosmos.cosmoschangefeed.testutils.TestData;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

@RunWith(MockitoJUnitRunner.class)
public class CosmosIngestionServiceTest {

  @Mock
  ReactiveCosmosRepository<Type1Entity, String> repository;

  @Mock
  IItemEntityCreator<Type1Item, Type1Entity> entityCreator;

  @Captor
  ArgumentCaptor<Job> jobCaptor;

  @Captor
  ArgumentCaptor<OffsetDateTime> timestampCaptor;

  @Captor
  ArgumentCaptor<Type1Entity> type1EntityCaptor;
  
  @Test
  public void ingest_whenValidInputIsProvided_thenEntitiesAreInserted() {
    // Arrange
    Job job = TestData.getValidJob(DataType.TYPE1);
    OffsetDateTime timestamp = TestData.getValidTimestamp();
    List<Type1Item> items = TestData.getValidType1Items();
    List<Type1Entity> entities = items.stream()
        .map(item -> createType1Entity(job, timestamp, item))
        .collect(Collectors.toList());

    when(entityCreator.create(any(Job.class), any(OffsetDateTime.class), any(Type1Item.class)))
        .thenAnswer(i -> createType1Entity(i.getArgument(0), i.getArgument(1), i.getArgument(2)));

    when(repository.save(any())).thenAnswer(i -> Mono.just(i.getArgument(0)));

    CosmosIngestionService<Type1Item, Type1Entity> service =
        new CosmosIngestionService<Type1Item, Type1Entity>(repository, entityCreator);

    // Act
    int failedIngestions = service.ingest(job, timestamp, items.iterator());

    // Assert
    assertThat(failedIngestions).isEqualTo(0);

    verify(repository, times(entities.size())).save(type1EntityCaptor.capture());
    List<Type1Entity> capturedEntities = type1EntityCaptor.getAllValues();
    assertThat(capturedEntities).usingRecursiveComparison().isEqualTo(entities);
  }

  @Test
  public void ingest_whenNoJobIsProvided_thenExceptionIsThrown() {
    // Arrange
    OffsetDateTime timestamp = TestData.getValidTimestamp();
    List<Type1Item> items = TestData.getValidType1Items();
    
    CosmosIngestionService<Type1Item, Type1Entity> service =
        new CosmosIngestionService<Type1Item, Type1Entity>(repository, entityCreator);

    // Act & Assert
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> {
          service.ingest(null, timestamp, items.iterator());
        });
  }
  
  @Test
  public void ingest_whenRepositoryFailsToInsertEntities_thenNumberOfFailedIngestionsIsReturned() {
    // Arrange
    Job job = TestData.getValidJob(DataType.TYPE1);
    OffsetDateTime timestamp = TestData.getValidTimestamp();
    List<Type1Item> items = TestData.getValidType1Items();

    when(entityCreator.create(any(Job.class), any(OffsetDateTime.class), any(Type1Item.class)))
        .thenAnswer(i -> createType1Entity(i.getArgument(0), i.getArgument(1), i.getArgument(2)));

    when(repository.save(any())).thenThrow(CosmosAccessException.class);

    CosmosIngestionService<Type1Item, Type1Entity> service =
        new CosmosIngestionService<Type1Item, Type1Entity>(repository, entityCreator);

    // Act
    int failedIngestions = service.ingest(job, timestamp, items.iterator());

    // Assert
    assertThat(failedIngestions).isEqualTo(items.size());
  }

  private Type1Entity createType1Entity(Job job, OffsetDateTime itemTimestamp, Type1Item item) {
    return Type1Entity.builder()
        .id(job.getSourceId() + "-" + item.getId())
        .partitionKey(job.getSourceId())
        .jobId(job.getId())
        .itemTimestamp(itemTimestamp)
        .item(item)
        .build();
  }

}
