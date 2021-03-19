package com.microsoft.cosmos.cosmoschangefeed.testutils;

import com.microsoft.cosmos.cosmoschangefeed.model.DataType;
import com.microsoft.cosmos.cosmoschangefeed.model.Job;
import com.microsoft.cosmos.cosmoschangefeed.model.Type1Item;
import com.microsoft.cosmos.cosmoschangefeed.model.Type2Item;
import com.microsoft.cosmos.cosmoschangefeed.testmodel.JobData;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Test data.
 */
public class TestData {

  /**
   * Get a valid timestamp.
   * @return timestamp
   */
  public static OffsetDateTime getValidTimestamp() {
    return OffsetDateTime.parse("2021-01-13T16:21:24.144595Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

  /**
   * Get a valid job.
   * @return job
   */
  public static Job getValidJob(DataType dataType) {
    String id = UUID.randomUUID().toString();
    return Job.builder()
      .id(id)
      .sourceId("company " + id)
      .dataType(dataType)
      .dataContainer("company " + id)
      .dataPath("2021/1/13/" + dataType.getValue() + ".json")
      .build();
  }

  /**
   * Get a list of valid type 1 items.
   * @return list
   */
  public static List<Type1Item> getValidType1Items() {
    return Arrays.asList(getValidType1Item(), getValidType1Item());
  }

  /**
   * Get a valid type 1 item.
   * @return item
   */
  public static Type1Item getValidType1Item() {
    String id = UUID.randomUUID().toString();
    return Type1Item.builder()
      .id(id)
      .name("name " + id)
      .description("description " + id)
      .build();
  }

  /**
   * Get a valid type 2 item.
   * @return item
   */
  public static Type2Item getValidType2Item() {
    return Type2Item.builder()
      .id(UUID.randomUUID().toString())
      .referenceId(UUID.randomUUID().toString())
      .quantity(ThreadLocalRandom.current().nextInt(1, 100))
      .build();
  }

  /**
   * Get a valid data header.
   * @return data header
   */
  public static JobData getValidType1JobData() {
    return JobData.builder()
        .timestamp(getValidTimestamp())
        .items(getValidType1Items())
        .build();
  }

}
