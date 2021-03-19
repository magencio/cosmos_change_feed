package com.microsoft.cosmos.cosmoschangefeed.processors;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Processor of changes in the database container.
 */
public interface IProcessor {

  /**
   * Process one change in the container.
   * @param document json that was added to/updated in the container
   */
  void process(JsonNode document);

}
