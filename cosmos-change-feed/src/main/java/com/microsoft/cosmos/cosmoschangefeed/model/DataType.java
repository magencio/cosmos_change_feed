package com.microsoft.cosmos.cosmoschangefeed.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Type of the data referred by a job.
 */
@AllArgsConstructor
@NoArgsConstructor
public enum DataType {
  TYPE1("type1"),
  TYPE2("type2");

  private String value;

  @JsonValue
  public String getValue() {
    return this.value;
  }

}
