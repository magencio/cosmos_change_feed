package com.microsoft.cosmos.cosmoschangefeed.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.microsoft.cosmos.cosmoschangefeed.mappers.OffsetDateTimeDeserializer;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Header of the data referred by a job.
 */
@Data
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataHeader {

  @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
  private OffsetDateTime timestamp;

  @JsonCreator
  public DataHeader(
      @JsonProperty(value = "timestamp", required = true) OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

}
