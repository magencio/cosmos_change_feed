package com.microsoft.cosmos.cosmoschangefeed.services.datasource.storage;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Service to access data from Storage.
 */
public interface IStorageService {

  /**
   * Download json document.
   * @param container where the json document can be found
   * @param path to the json document within the container
   * @return json
   */
  JsonNode downloadJson(String container, String path);

}
