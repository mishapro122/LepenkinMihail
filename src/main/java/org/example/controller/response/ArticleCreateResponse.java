package org.example.controller.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ArticleCreateResponse {
    private final long id;

    @JsonCreator
    public ArticleCreateResponse(@JsonProperty("id") long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
