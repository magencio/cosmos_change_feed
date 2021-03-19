package com.microsoft.cosmos.cosmoschangefeed.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * An item contained in the data referred by a job.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class Item {

  private String id;

}
