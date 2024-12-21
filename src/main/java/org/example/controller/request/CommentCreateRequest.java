package org.example.controller.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentCreateRequest {
    private final long articleId;
    private final String text;
    @JsonCreator
    public CommentCreateRequest(@JsonProperty("articleId") long articleId, @JsonProperty("text") String text){
        this.articleId=articleId;
        this.text=text;
    }

    public long getArticleId() {
        return articleId;
    }

    public String getText() {
        return text;
    }
}
