package com.microsoft.cosmos.cosmoschangefeed.services.ingestion;

public interface IIngestionServiceFactory {

  IIngestionService getService(String id);

}
