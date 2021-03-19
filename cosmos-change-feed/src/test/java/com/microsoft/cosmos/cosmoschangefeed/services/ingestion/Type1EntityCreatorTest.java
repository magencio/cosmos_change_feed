package com.microsoft.cosmos.cosmoschangefeed.services.ingestion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.microsoft.cosmos.cosmoschangefeed.model.DataType;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.model.Type1Item;
import com.microsoft.cosmos.cosmoschangefeed.model.Type2Item;
import com.microsoft.cosmos.cosmoschangefeed.persistence.entities.Type1Entity;
import com.microsoft.cosmos.cosmoschangefeed.services.ingestion.cosmos.Type1EntityCreator;
import com.microsoft.cosmos.cosmoschangefeed.testutils.TestData;
import java.time.OffsetDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Type1EntityCreatorTest {

  @Test
  public void create_whenValidDataIsProvided_thenEntityIsCreated() {
    // Arrange
    Job job = TestData.getValidJob(DataType.TYPE1);
    OffsetDateTime timestamp = TestData.getValidTimestamp();
    Type1Item item = TestData.getValidType1Item();

    Type1Entity expectedEntity = Type1Entity.builder()
        .id(job.getSourceId() + "-" + item.getId())
        .partitionKey(job.getSourceId())
        .jobId(job.getId())
        .itemTimestamp(timestamp)
        .item(item)
        .build();

    Type1EntityCreator creator = new Type1EntityCreator();

    // Act
    Type1Entity entity = creator.create(job, timestamp, item);
    
    // Assert
    assertThat(entity).usingRecursiveComparison().isEqualTo(expectedEntity);
  }

  @Test
  public void create_whenInvalidItemTypeIsProvided_thenExceptionIsThrown() {
    // Arrange
    Job job = TestData.getValidJob(DataType.TYPE1);
    OffsetDateTime timestamp = TestData.getValidTimestamp();
    Type2Item item = TestData.getValidType2Item();

    Type1EntityCreator creator = new Type1EntityCreator();

    // Act & Assert
    assertThatExceptionOfType(ClassCastException.class)
        .isThrownBy(() -> {
          creator.create(job, timestamp, item);
        });
  }

}