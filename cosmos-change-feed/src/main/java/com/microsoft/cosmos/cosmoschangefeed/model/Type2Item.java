package com.microsoft.cosmos.cosmoschangefeed.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Item of type 2.
 */
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Type2Item extends Item {

  private String referenceId;
  private Integer quantity;

  @JsonCreator
  public Type2Item(
      @JsonProperty(value = "id", required = true) String id,
      @JsonProperty(value = "referenceId", required = true) String referenceId,
      @JsonProperty(value = "quantity", required = true) Integer quantity) {
    super(id);
    this.referenceId = referenceId;
    this.quantity = quantity;
  }

}
