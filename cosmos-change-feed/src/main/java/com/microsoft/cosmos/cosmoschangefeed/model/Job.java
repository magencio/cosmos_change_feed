package com.microsoft.cosmos.cosmoschangefeed.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A job that refers to the data to ingest.
 */
@Data
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {

  private String id;
  private String sourceId;
  private DataType dataType;
  private String dataContainer;
  private String dataPath;

  /**
   * Initializes a job.
   * @param id of the job
   * @param sourceId of the source of the job (e.g. a company)
   * @param dataType with the type of the data
   * @param dataContainer with the name of the container where the data is stored
   * @param dataPath with the path to the data in the container
   */
  @JsonCreator
  public Job(
      @JsonProperty(value = "id", required = true) String id,
      @JsonProperty(value = "sourceId", required = true) String sourceId,
      @JsonProperty(value = "dataType", required = true) DataType dataType,
      @JsonProperty(value = "dataContainer", required = true) String dataContainer,
      @JsonProperty(value = "dataPath", required = true) String dataPath) {
    this.id = id;
    this.sourceId = sourceId;
    this.dataType = dataType;
    this.dataContainer = dataContainer;
    this.dataPath = dataPath;
  }

}
