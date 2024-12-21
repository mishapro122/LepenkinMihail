package org.example.controller.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class ArticleGetResponse {
    private final String name;
    private final String tags;
    private final String commentList;
    private final boolean trending;
    @JsonCreator
    public ArticleGetResponse(@JsonProperty("name") String name, @JsonProperty("tags") String tags, @JsonProperty("commentList") String commentList, @JsonProperty("trending") boolean trending) {
        this.name = name;
        this.tags = tags;
        this.commentList = commentList;
        this.trending = trending;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name,tags,commentList, trending);
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof ArticleGetResponse articleGetResponse){
            return Objects.equals(articleGetResponse.name,name) && Objects.equals(articleGetResponse.tags, tags) && Objects.equals(articleGetResponse.commentList, commentList) && Objects.equals(articleGetResponse.trending, trending);
        }
        return false;
    }

    public String getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

    public String getCommentList() {
        return commentList;
    }

    public boolean isTrending() {
        return trending;
    }
}
