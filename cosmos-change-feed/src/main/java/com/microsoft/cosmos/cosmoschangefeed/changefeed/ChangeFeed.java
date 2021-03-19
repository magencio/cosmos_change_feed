package com.microsoft.cosmos.cosmoschangefeed.changefeed;

import com.azure.cosmos.ChangeFeedProcessor;
import com.azure.cosmos.ChangeFeedProcessorBuilder;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosAsyncDatabase;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.models.ChangeFeedProcessorOptions;
import com.fasterxml.jackson.databind.JsonNode;
import com.microsoft.cosmos.cosmoschangefeed.configuration.CosmosConfiguration;
import com.microsoft.cosmos.cosmoschangefeed.exceptions.ProcessException;
import com.microsoft.cosmos.cosmoschangefeed.processors.IProcessor;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

/**
 * CosmosDB Change Feed that hanles changes in a container.
 */
@Component
public class ChangeFeed implements IChangeFeed {

  private final int pollDelay = 100;
  private static AtomicBoolean isProcessorRunning = new AtomicBoolean(false);

  private ChangeFeedProcessor changeFeedProcessor;

  /**
   * Initializes a CosmosDB change feed.
   * @param configuration to access CosmosDB
   * @param processor to process the changes in the container
   */
  @Autowired
  public ChangeFeed(CosmosConfiguration configuration, IProcessor processor) {
    CosmosAsyncClient client = new CosmosClientBuilder()
        .endpoint(configuration.getUri())
        .key(configuration.getKey())
        .contentResponseOnWriteEnabled(true)
        .buildAsyncClient();
    CosmosAsyncDatabase database = client.getDatabase(configuration.getDatabase());
    CosmosAsyncContainer feedContainer = database.getContainer(configuration.getFeedContainer());
    CosmosAsyncContainer leaseContainer = database.getContainer(configuration.getLeaseContainer());
    ChangeFeedProcessorOptions options = new ChangeFeedProcessorOptions();
    options.setFeedPollDelay(Duration.ofMillis(pollDelay));
    options.setStartFromBeginning(true);
    this.changeFeedProcessor = new ChangeFeedProcessorBuilder()
        .options(options)
        .hostName(UUID.randomUUID().toString())
        .feedContainer(feedContainer)
        .leaseContainer(leaseContainer)
        .handleChanges((List<JsonNode> documents) -> {
          handleChanges(documents, processor);
        })
        .buildChangeFeedProcessor();
  }

  @Override
  public void start() {
    changeFeedProcessor
        .start()
        .subscribeOn(Schedulers.elastic())
        .doOnSuccess(run -> isProcessorRunning.set(true))
        .subscribe();

    while (!isProcessorRunning.get()) {
      // Wait for Change Feed processor to start
    }
  }

  private void handleChanges(List<JsonNode> documents, IProcessor processor) {
    for (JsonNode document : documents) {
      try {
        processor.process(document);
      } catch (ProcessException ex) {
        ex.printStackTrace();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

}
