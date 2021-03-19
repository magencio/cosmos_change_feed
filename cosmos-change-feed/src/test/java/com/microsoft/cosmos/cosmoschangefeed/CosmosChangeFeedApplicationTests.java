package com.microsoft.cosmos.cosmoschangefeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class CosmosChangeFeedApplicationTests {
  public static void main(String[] args) {
    SpringApplication.run(CosmosChangeFeedApplication.class, args);
  }
}
