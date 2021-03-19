package com.microsoft.cosmos.cosmoschangefeed.exceptions;

@SuppressWarnings("serial")
public class InvalidJobException extends RuntimeException {

  public InvalidJobException(String message) {
    super(message);
  }

  public InvalidJobException(String message, Throwable ex) {
    super(message, ex);
  }

}
