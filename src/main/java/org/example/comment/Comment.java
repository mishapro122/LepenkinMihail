package org.example.comment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.article.ArticleId;

import java.util.Objects;

public class Comment {
    private final CommentId id;
    private final ArticleId articleId;
    private final String text;

    @JsonCreator
    public Comment(@JsonProperty("id") CommentId id,@JsonProperty("articleId") ArticleId articleId,@JsonProperty("text") String text) {
        this.id = id;
        this.articleId = articleId;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public ArticleId getArticleId() {
        return articleId;
    }

    public long getId() {
        return id.getValue();
    }
    public Comment withText(String newText){
        return new Comment(this.id,this.articleId,newText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, articleId, text);
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof  Comment comment){
            return Objects.equals(id, comment.id) && Objects.equals(articleId, comment.articleId) && Objects.equals(text, comment.text);
        }
        return false;
    }

    @Override
    public String toString(){
        return text;
    }
}
