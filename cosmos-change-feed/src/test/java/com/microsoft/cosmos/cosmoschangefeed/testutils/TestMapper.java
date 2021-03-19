package com.microsoft.cosmos.cosmoschangefeed.testutils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Test mappers.
 */
public class TestMapper {

  /**
   * Get a mapper that we can use to serialize objects to json.
   * @return mapper
   */
  public static ObjectMapper getSerializerMapper() {
    JsonSerializer<OffsetDateTime> serializer = new JsonSerializer<OffsetDateTime>() {
      @Override
      public void serialize(OffsetDateTime offsetDateTime, JsonGenerator jsonGenerator,
          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSxxx").format(offsetDateTime));
      }
    };

    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addSerializer(OffsetDateTime.class, serializer);

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(simpleModule);
    return mapper;
  }
}
