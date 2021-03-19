package com.microsoft.cosmos.cosmoschangefeed.services.ingestion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.microsoft.cosmos.cosmoschangefeed.model.DataType;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.model.Type1Item;
import com.microsoft.cosmos.cosmoschangefeed.model.Type2Item;
import com.microsoft.cosmos.cosmoschangefeed.persistence.entities.Type2Entity;
import com.microsoft.cosmos.cosmoschangefeed.services.ingestion.cosmos.Type2EntityCreator;
import com.microsoft.cosmos.cosmoschangefeed.testutils.TestData;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Type2EntityCreatorTest {

  @Test
  public void create_whenValidDataIsProvided_thenEntityIsCreated() {
    // Arrange
    Job job = TestData.getValidJob(DataType.TYPE2);
    OffsetDateTime timestamp = OffsetDateTime.parse("2021-01-13T16:21:24.144595Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    Type2Item item = TestData.getValidType2Item();

    Type2Entity expectedEntity = Type2Entity.builder()
        .id(job.getSourceId() + "-" + item.getId())
        .partitionKey(job.getSourceId())
        .jobId(job.getId())
        .itemTimestamp(timestamp)
        .item(item)
        .build();

    Type2EntityCreator creator = new Type2EntityCreator();

    // Act
    Type2Entity entity = creator.create(job, timestamp, item);
    
    // Assert
    assertThat(entity).usingRecursiveComparison().isEqualTo(expectedEntity);
  }

  @Test
  public void create_whenInvalidItemTypeIsProvided_thenExceptionIsThrown() {
    // Arrange
    Job job = TestData.getValidJob(DataType.TYPE2);
    OffsetDateTime timestamp = OffsetDateTime.parse("2021-01-13T16:21:24.144595Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    Type1Item item = TestData.getValidType1Item();

    Type2EntityCreator creator = new Type2EntityCreator();

    // Act & Assert
    assertThatExceptionOfType(ClassCastException.class)
        .isThrownBy(() -> {
          creator.create(job, timestamp, item);
        });
  }

}