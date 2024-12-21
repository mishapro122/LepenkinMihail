package org.example.controller.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentUpdateRequest {
    private final long articleId;
    private final String text;
    @JsonCreator
    public CommentUpdateRequest(@JsonProperty("articleId") long articleId, @JsonProperty("text") String text){
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
