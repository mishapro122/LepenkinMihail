package org.example.controller.response;

public class CommentUpdateResponse {
    private final long id;

    public CommentUpdateResponse(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
