package com.microsoft.cosmos.cosmoschangefeed;

import com.microsoft.cosmos.cosmoschangefeed.changefeed.IChangeFeed;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CosmosChangeFeedApplication {

  @Autowired
  private IChangeFeed changeFeed;

  public static void main(String[] args) {
    SpringApplication.run(CosmosChangeFeedApplication.class, args);
  }

  @PostConstruct
  public void start() throws Exception {
    changeFeed.start();
  }
}
