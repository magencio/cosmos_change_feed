package com.microsoft.cosmos.cosmoschangefeed.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microsoft.cosmos.cosmoschangefeed.mappers.OffsetDateTimeDeserializer;
import com.microsoft.cosmos.cosmoschangefeed.mappers.OffsetDateTimeSerializer;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An entry in the log of states for a job.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobStateEntry {

  @JsonSerialize(using = OffsetDateTimeSerializer.class)
  @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
  private OffsetDateTime timestamp;

  private JobState state;
  private int failedIngestions;
  private String errorMessage;

}
