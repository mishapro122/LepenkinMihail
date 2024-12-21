package org.example.exceptions;

public class TopicIdDuplicatedException extends RuntimeException {
  public TopicIdDuplicatedException(String message) {
    super(message);
  }
}
