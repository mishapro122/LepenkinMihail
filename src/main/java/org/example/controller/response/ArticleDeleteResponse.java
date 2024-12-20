package org.example.controller.response;

public class ArticleDeleteResponse {
    private final long id;

    public ArticleDeleteResponse(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
