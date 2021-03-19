package com.microsoft.cosmos.cosmoschangefeed.services.datasource.storage;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.cosmos.cosmoschangefeed.model.DataHeader;
import com.microsoft.cosmos.cosmoschangefeed.model.Item;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.services.datasource.IDataSourceService;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

/**
 * Service to access job related data from Storage.
 */
public class StorageDataSourceService implements IDataSourceService {

  protected IStorageService storageService;

  protected Class<?> itemClass;

  /**
   * Creates a new instance of StorageDataSourceService.
   * @param storageService to access data from Storage
   * @param itemClass class of the items in the data
   */
  public StorageDataSourceService(IStorageService storageService, Class<? extends Item> itemClass) {
    this.storageService = storageService;
    this.itemClass = itemClass;
  }

  @Override
  public OffsetDateTime getTimestamp(Job job) {
    if (job == null) {
      throw new NullPointerException("Job not set");
    }

    return getHeader(job).getTimestamp();
  }

  @Override
  public Iterator<? extends Item> getItems(Job job) {
    if (job == null) {
      throw new NullPointerException("Job not set");
    }

    // We are downloading the complete document to get all the items at once.
    // To work with big files, we would have to download the file in chunks and get the items from those chunks.
    JsonNode jsonNode = storageService.downloadJson(job.getDataContainer(), job.getDataPath());
    JsonNode jsonItems = jsonNode.get("items");
    
    List<? extends Item> items = convertItems(jsonItems);
    return items.iterator();
  }

  protected DataHeader getHeader(Job job) {
    // We are downloading the complete document just to get the header.
    // To work with big files, we would have to download the file in chunks and get the header from the first chunk.
    JsonNode jsonNode = storageService.downloadJson(job.getDataContainer(), job.getDataPath());
    String timestampString = jsonNode.get("timestamp").asText();
    OffsetDateTime timestamp = OffsetDateTime.parse(timestampString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

    return DataHeader.builder()
        .timestamp(timestamp)
        .build();
  }

  private List<? extends Item> convertItems(JsonNode jsonItems) {
    ObjectMapper mapper = new ObjectMapper();
    JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, itemClass);
    return mapper.convertValue(jsonItems, listType);
  }

}
