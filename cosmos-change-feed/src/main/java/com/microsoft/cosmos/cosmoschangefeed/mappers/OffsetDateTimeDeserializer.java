package com.microsoft.cosmos.cosmoschangefeed.mappers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("serial")
public class OffsetDateTimeDeserializer extends StdDeserializer<OffsetDateTime> {

  public OffsetDateTimeDeserializer() {
    this(null);
  }

  public OffsetDateTimeDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public OffsetDateTime deserialize(JsonParser jsonparser, DeserializationContext context)
      throws IOException {
    try {
      return OffsetDateTime.parse(jsonparser.getText(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
