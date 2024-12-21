package org.example.controller.request;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;
public class ArticleCreateRequest {
    private final String name;
    private final Set<String> tags;
    @JsonCreator
    public ArticleCreateRequest(@JsonProperty("name") String name, @JsonProperty("tags") Set<String> tags) {
        this.name = name;
        this.tags = tags;
    }

    public Set<String> getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }
}
