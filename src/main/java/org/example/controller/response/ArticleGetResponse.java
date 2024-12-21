package org.example.controller.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.comment.Comment;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ArticleGetResponse {
    private final String name;
    private final Set<String> tags;
    private final List<Comment> commentList;
    @JsonCreator
    public ArticleGetResponse(@JsonProperty("name") String name, @JsonProperty("tags") Set<String> tags, @JsonProperty("commentList") List<Comment> commentList) {
        this.name = name;
        this.tags = tags;
        this.commentList = commentList;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name,tags,commentList);
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof ArticleGetResponse articleGetResponse){
            return Objects.equals(articleGetResponse.name,name) && Objects.equals(articleGetResponse.tags, tags) && Objects.equals(articleGetResponse.commentList, commentList);
        }
        return false;
    }

    public Set<String> getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }
}
