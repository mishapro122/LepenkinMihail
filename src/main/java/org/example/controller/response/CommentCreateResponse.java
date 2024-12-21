package org.example.controller.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentCreateResponse {
    private final long id;
    @JsonCreator
    public CommentCreateResponse(@JsonProperty("id") long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
