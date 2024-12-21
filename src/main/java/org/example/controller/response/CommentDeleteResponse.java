package org.example.controller.response;

public class CommentDeleteResponse {
    private final long id;

    public CommentDeleteResponse(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
