package com.microsoft.cosmos.cosmoschangefeed.testmodel;

import com.microsoft.cosmos.cosmoschangefeed.model.Item;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data referred by a job.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobData {

  // Header
  private OffsetDateTime timestamp;

  // Items
  private List<? extends Item> items;

}
