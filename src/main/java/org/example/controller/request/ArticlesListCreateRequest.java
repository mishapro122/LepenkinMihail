package org.example.controller.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArticlesListCreateRequest {
    private final List<ArticleCreateRequest> articlesMap;
    @JsonCreator
    public ArticlesListCreateRequest(@JsonProperty("articles") List<ArticleCreateRequest> articlesMap) {
        this.articlesMap = articlesMap;
    }

    public List<ArticleCreateRequest> getArticlesList() {
        return articlesMap;
    }
}
