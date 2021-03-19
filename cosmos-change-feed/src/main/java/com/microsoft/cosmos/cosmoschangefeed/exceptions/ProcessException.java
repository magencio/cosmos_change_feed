package com.microsoft.cosmos.cosmoschangefeed.exceptions;

@SuppressWarnings("serial")
public class ProcessException extends RuntimeException {
  
  public ProcessException(String message, Throwable ex) {
    super(message, ex);
  }

}
