package org.example.controller.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Set;

public class ArticlesListCreateResponse {
    private final Set<Map<String,Long>> articlesMap;
    @JsonCreator
    public ArticlesListCreateResponse(@JsonProperty("articles") Set<Map<String, Long>> articlesMap) {
        this.articlesMap = articlesMap;
    }

    public Set<Map<String, Long>> getArticlesMap() {
        return articlesMap;
    }
}
