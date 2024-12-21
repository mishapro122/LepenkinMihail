package org.example.article;

import org.example.comment.Comment;
import org.example.topic.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Article {
    private final ArticleId id;
    private final String name;
    private final Set<Topic> tags;
    private final List<Comment> comments;
    private final boolean trending;

    public Article(ArticleId id, String name, Set<Topic> tags, List<Comment> comments, boolean trending) {
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.comments = comments;
        this.trending = trending;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getCommentsString(){ return comments.toString();}

    public long getId() {
        return id.getValue();
    }

    public Set<Topic> getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

    public boolean isTrending() {
        return trending;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,name,tags,comments,trending);
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Article article){
            return Objects.equals(id,article.id) && Objects.equals(name, article.name) && Objects.equals(tags, article.tags) && Objects.equals(comments, article.comments) && Objects.equals(trending, article.trending);
        }
        return false;
    }

    public Article withName(String newName){
        return new Article(this.id,newName,this.tags,this.comments, trending);
    }
    public Article withComments(List<Comment> newComments){
        if (newComments == null) return new Article(this.id, this.name, this.tags, new ArrayList<>(), false);
        if (newComments.size()<=3){
            return new Article(this.id,this.name,this.tags,newComments, false);
        }
        else {
            return new Article(this.id, this.name, this.tags, newComments, true);
        }
    }
    public Article withTags(Set<Topic> tags){
        return new Article(this.id,this.name,tags,this.comments, trending);
    }

    public Article withTrending(boolean newTrending){
        return new Article(id,name,tags,comments,newTrending);
    }
}
