package org.example.exceptions;

public class CommentDeleteException extends RuntimeException {
    public CommentDeleteException(String message) {
        super(message);
    }
}
