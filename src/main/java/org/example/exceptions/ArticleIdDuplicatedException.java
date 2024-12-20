package org.example.exceptions;

public class ArticleIdDuplicatedException extends RuntimeException {
    public ArticleIdDuplicatedException(String message) {
        super(message);
    }
}
