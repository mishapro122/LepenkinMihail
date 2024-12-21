package org.example.commentrepository;

import org.example.comment.Comment;
import org.example.comment.CommentId;

import java.util.List;

public interface CommentRepository {
    CommentId generateId();

    List<Comment> findAll();

    Comment findById(long commentId);

    void create(Comment comment);

    void update(Comment comment);

    void delete(long commentId);
}
