package com.microsoft.cosmos.cosmoschangefeed.configuration;

import com.microsoft.cosmos.cosmoschangefeed.model.Type1Item;
import com.microsoft.cosmos.cosmoschangefeed.model.Type2Item;
import com.microsoft.cosmos.cosmoschangefeed.persistence.entities.Type1Entity;
import com.microsoft.cosmos.cosmoschangefeed.persistence.entities.Type2Entity;
import com.microsoft.cosmos.cosmoschangefeed.persistence.repositories.Type1Repository;
import com.microsoft.cosmos.cosmoschangefeed.persistence.repositories.Type2Repository;
import com.microsoft.cosmos.cosmoschangefeed.services.datasource.IDataSourceService;
import com.microsoft.cosmos.cosmoschangefeed.services.datasource.IDataSourceServiceFactory;
import com.microsoft.cosmos.cosmoschangefeed.services.datasource.storage.IStorageService;
import com.microsoft.cosmos.cosmoschangefeed.services.datasource.storage.StorageDataSourceService;
import com.microsoft.cosmos.cosmoschangefeed.services.ingestion.IIngestionService;
import com.microsoft.cosmos.cosmoschangefeed.services.ingestion.IIngestionServiceFactory;
import com.microsoft.cosmos.cosmoschangefeed.services.ingestion.cosmos.CosmosIngestionService;
import com.microsoft.cosmos.cosmoschangefeed.services.ingestion.cosmos.Type1EntityCreator;
import com.microsoft.cosmos.cosmoschangefeed.services.ingestion.cosmos.Type2EntityCreator;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {

  // Service locator factories

  @Bean
  public ServiceLocatorFactoryBean createDataSourceServiceLocatorFactoryBean() {
    ServiceLocatorFactoryBean bean = new ServiceLocatorFactoryBean();
    bean.setServiceLocatorInterface(IDataSourceServiceFactory.class);
    return bean;
  }

  @Bean
  public ServiceLocatorFactoryBean createIngestionServiceLocatorFactoryBean() {
    ServiceLocatorFactoryBean bean = new ServiceLocatorFactoryBean();
    bean.setServiceLocatorInterface(IIngestionServiceFactory.class);
    return bean;
  }

  // Data Source services

  @Bean("TYPE1DataSourceService")
  public IDataSourceService createType1DataSourceService(IStorageService storageService) {
    return new StorageDataSourceService(storageService, Type1Item.class);
  }

  @Bean("TYPE2DataSourceService")
  public IDataSourceService createType2DataSourceService(IStorageService storageService) {
    return new StorageDataSourceService(storageService, Type2Item.class);
  }

  // Ingestion services

  @Bean("TYPE1IngestionService")
  public IIngestionService createType1IngestionService(Type1Repository repository, Type1EntityCreator entityCreator) {
    return new CosmosIngestionService<Type1Item, Type1Entity>(repository, entityCreator);
  }

  @Bean("TYPE2IngestionService")
  public IIngestionService createType2IngestionService(Type2Repository repository, Type2EntityCreator entityCreator) {
    return new CosmosIngestionService<Type2Item, Type2Entity>(repository, entityCreator);
  }

}

