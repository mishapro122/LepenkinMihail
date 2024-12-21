package org.example.exceptions;

public class ArticleUpdateException extends RuntimeException {
    public ArticleUpdateException(String message) {
        super(message);
    }
}
