package com.microsoft.cosmos.cosmoschangefeed.services.datasource;

public interface IDataSourceServiceFactory {

  IDataSourceService getService(String id);

}
