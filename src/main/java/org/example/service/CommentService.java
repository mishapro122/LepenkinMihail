package org.example.service;

import org.example.article.Article;
import org.example.article.ArticleId;
import org.example.articlerepository.ArticleRepository;
import org.example.comment.Comment;
import org.example.comment.CommentId;
import org.example.commentrepository.CommentRepository;
import org.example.exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class CommentService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public CommentService(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    public Comment findCommentById(long id){
        try{
            return commentRepository.findById(id);
        }
        catch (CommentNotFoundException e){
            throw new CommentNotFoundException("cannot find comment with id="+id);
        }
    }

    public long createComment(long articleId, String text) {
        CommentId commentId = commentRepository.generateId();
        Comment comment=new Comment(commentId, new ArticleId(articleId), text);
        Article article;
        List<Comment> commentList;
        try {
            article = articleRepository.findByIdForUpdate(articleId);
            commentList = article.getComments();
            commentList.add(comment);
            commentRepository.create(comment);
            articleRepository.update(article.withComments(commentList));
            return comment.getId();
        }catch (CommentIdDuplicatedException | ArticleNotFoundException e){
            throw new CommentCreateException("cannot create comment");
        }
    }

    public void commentUpdate(long commentId, String text){
        Comment newComment;
        try{
            newComment =commentRepository.findById(commentId);
        }
        catch (CommentNotFoundException e){
            throw new CommentUpdateException("cannot find comment with id="+commentId);
        }
        commentRepository.update(newComment.withText(text));
    }
    public void deleteComment(long commentId){
        Comment comment;
        try {
            comment=commentRepository.findById(commentId);
        }catch (CommentNotFoundException e){
            throw new CommentDeleteException("cannot delete comment with id="+commentId);
        }
        commentRepository.delete(commentId);
    }
}
