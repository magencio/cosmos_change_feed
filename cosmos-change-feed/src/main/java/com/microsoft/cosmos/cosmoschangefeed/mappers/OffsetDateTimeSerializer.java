package com.microsoft.cosmos.cosmoschangefeed.mappers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("serial")
public class OffsetDateTimeSerializer extends StdSerializer<OffsetDateTime> {

  public OffsetDateTimeSerializer() {
    this(null);
  }

  protected OffsetDateTimeSerializer(Class<OffsetDateTime> t) {
    super(t);
  }

  @Override
  public void serialize(OffsetDateTime offsetDateTime, JsonGenerator jsonGenerator, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
    jsonGenerator
        .writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSxxx")
        .format(offsetDateTime));
  }

}
