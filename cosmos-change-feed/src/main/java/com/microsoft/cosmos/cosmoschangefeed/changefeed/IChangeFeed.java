package com.microsoft.cosmos.cosmoschangefeed.changefeed;

/**
 * Change Feed that hanles changes in a database container.
 */
public interface IChangeFeed {

  /**
   * Start the Change Feed.
   */
  void start();

}
