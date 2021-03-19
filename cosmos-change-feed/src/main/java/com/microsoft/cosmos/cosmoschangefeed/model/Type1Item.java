package com.microsoft.cosmos.cosmoschangefeed.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Item of type 1.
 */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Type1Item extends Item {

  private String name;
  private String description;

  @JsonCreator
  public Type1Item(
      @JsonProperty(value = "id", required = true) String id,
      @JsonProperty(value = "name", required = true) String name,
      @JsonProperty(value = "description", required = false) String description) {
    super(id);
    this.name = name;
    this.description = description;
  }

}
