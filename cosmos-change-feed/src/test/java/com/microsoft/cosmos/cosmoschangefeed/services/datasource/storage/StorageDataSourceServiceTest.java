package com.microsoft.cosmos.cosmoschangefeed.services.datasource.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microsoft.cosmos.cosmoschangefeed.model.DataType;
import com.microsoft.cosmos.cosmoschangefeed.model.Item;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.model.Type1Item;
import com.microsoft.cosmos.cosmoschangefeed.model.Type2Item;
import com.microsoft.cosmos.cosmoschangefeed.testmodel.JobData;
import com.microsoft.cosmos.cosmoschangefeed.testutils.TestData;
import com.microsoft.cosmos.cosmoschangefeed.testutils.TestMapper;
import com.microsoft.cosmos.cosmoschangefeed.testutils.TestUtils;
import java.time.OffsetDateTime;
import java.util.Iterator;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StorageDataSourceServiceTest {

  ObjectMapper mapper;

  @Mock
  IStorageService storageService;

  /**
   * Run before every test.
   */
  @Before
  public void init() {
    this.mapper = TestMapper.getSerializerMapper();
  }

  @Test
  public void getTimestamp_whenValidJobIsProvided_thenTimestampIsReturned() {
    // Arrange
    Job job = TestData.getValidJob(DataType.TYPE1);
    String container = job.getDataContainer();
    String path = job.getDataPath();

    JobData jobData = TestData.getValidType1JobData();
    JsonNode json = mapper.valueToTree(jobData);
    when(storageService.downloadJson(container, path)).thenReturn(json);

    StorageDataSourceService service = new StorageDataSourceService(storageService, Type1Item.class);

    // Act
    OffsetDateTime timestamp = service.getTimestamp(job);

    // Assert
    assertThat(timestamp).isEqualTo(jobData.getTimestamp());
  }

  @Test
  public void getTimestamp_whenNoJobIsProvided_thenExceptionIsThrown() {
    // Arrange
    StorageDataSourceService service = new StorageDataSourceService(storageService, Type1Item.class);

    // Act & Assert
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> {
          service.getTimestamp(null);
        });
  }

  @Test
  public void getTimestamp_whenInvalidDataHeaderIsDownloaded_thenExceptionIsThrown() {
    // Arrange
    Job job = TestData.getValidJob(DataType.TYPE1);
    String container = job.getDataContainer();
    String path = job.getDataPath();

    JobData jobData = TestData.getValidType1JobData();
    JsonNode json = mapper.valueToTree(jobData);
    ((ObjectNode) json).remove("timestamp");
    when(storageService.downloadJson(container, path)).thenReturn(json);

    StorageDataSourceService service = new StorageDataSourceService(storageService, Type1Item.class);

    // Act & Assert
    assertThatExceptionOfType(Exception.class)
        .isThrownBy(() -> {
          service.getTimestamp(job);
        });
  }

  @Test
  public void getItems_whenValidJobIsProvided_thenItsReferredItemsAreReturned() {
    // Arrange
    Job job = TestData.getValidJob(DataType.TYPE1);
    String container = job.getDataContainer();
    String path = job.getDataPath();

    JobData jobData = TestData.getValidType1JobData();
    JsonNode json = mapper.valueToTree(jobData);
    when(storageService.downloadJson(container, path)).thenReturn(json);

    StorageDataSourceService service = new StorageDataSourceService(storageService, Type1Item.class);

    // Act
    Iterator<? extends Item> itemsIterator = service.getItems(job);

    // Assert
    List<? extends Item> items = TestUtils.toList(itemsIterator);
    assertThat(items).usingRecursiveComparison().isEqualTo(jobData.getItems());
  }

  @Test
  public void getItems_whenNoJobIsProvided_thenExceptionIsThrown() {
    // Arrange
    StorageDataSourceService service = new StorageDataSourceService(storageService, Type1Item.class);

    // Act & Assert
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> {
          service.getItems(null);
        });
  }

  @Test
  public void getItems_whenItemsOfInvalidTypeAreDownloaded_thenExceptionIsThrown() {
    // Arrange
    Job job = TestData.getValidJob(DataType.TYPE2);
    String container = job.getDataContainer();
    String path = job.getDataPath();

    JobData jobData = TestData.getValidType1JobData();
    JsonNode json = mapper.valueToTree(jobData);
    when(storageService.downloadJson(container, path)).thenReturn(json);

    StorageDataSourceService service = new StorageDataSourceService(storageService, Type2Item.class);

    // Assert
    assertThatExceptionOfType(Exception.class)
        .isThrownBy(() -> {
          service.getItems(job);
        });
  }

}
