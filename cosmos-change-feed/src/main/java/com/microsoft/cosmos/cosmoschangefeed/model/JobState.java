package com.microsoft.cosmos.cosmoschangefeed.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * State of the processing of a job.
 */
@AllArgsConstructor
@NoArgsConstructor
public enum JobState {
  QUEUED("queued"),
  PROCESSING("processing"),
  COMPLETED("completed"),
  ERROR("error");

  private String value;

  @JsonValue
  public String getValue() {
    return this.value;
  }

}
