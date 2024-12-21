package org.example.article;

import org.example.comment.Comment;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Article {
    private final ArticleId id;
    private final String name;
    private final Set<String> tags;
    private final List<Comment> comments;

    public Article(ArticleId id, String name, Set<String> tags, List<Comment> comments) {
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.comments = comments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getCommentsString(){ return comments.toString();}

    public long getId() {
        return id.getValue();
    }

    public Set<String> getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,name,tags,comments);
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Article article){
            return Objects.equals(id,article.id) && Objects.equals(name, article.name) && Objects.equals(tags, article.tags) && Objects.equals(comments, article.comments);
        }
        return false;
    }

    public Article withName(String newName){
        return new Article(this.id,newName,this.tags,this.comments);
    }
    public Article withComments(List<Comment> newComments){
        return new Article(this.id,this.name,this.tags,newComments);
    }
    public Article withTags(Set<String> tags){
        return new Article(this.id,this.name,tags,this.comments);
    }
}
