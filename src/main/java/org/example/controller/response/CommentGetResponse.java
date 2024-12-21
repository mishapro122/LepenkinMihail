package org.example.controller.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CommentGetResponse {
    private final long commentId;
    private final long articleId;
    private final String text;
    @JsonCreator
    public CommentGetResponse(@JsonProperty("commentId") long commentId, @JsonProperty("articleId") long articleId, @JsonProperty("text") String text) {
        this.commentId = commentId;
        this.articleId = articleId;
        this.text = text;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId,articleId,text);
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof CommentGetResponse commentGetResponse){
            return Objects.equals(commentGetResponse.commentId,commentId) && Objects.equals(commentGetResponse.articleId, articleId) && Objects.equals(commentGetResponse.text,text);
        }
        return false;
    }

    public String getText() {
        return text;
    }

    public long getArticleId() {
        return articleId;
    }

    public long getCommentId() {
        return commentId;
    }
}
