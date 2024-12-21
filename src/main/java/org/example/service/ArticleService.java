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
import java.util.Set;

public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public ArticleService(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }
    public List<Article> findAll(){
        return articleRepository.findAll();
    }
    public Article findArticleById(long id){
        try{
            return articleRepository.findById(id);
        } catch (ArticleNotFoundException e) {
            throw new ArticleNotFoundException("cannot find article with id="+id);
        }
    }

    public Comment findCommentById(long id){
        try{
            return commentRepository.findById(id);
        }
        catch (CommentNotFoundException e){
            throw new CommentNotFoundException("cannot find comment with id="+id);
        }
    }

    public long createArticle(String name, Set<String> tags){
        ArticleId articleId = articleRepository.generateId();
        Article article=new Article(articleId, name, tags, new ArrayList<>());
        try{
            articleRepository.create(article);
            return article.getId();
        }
        catch (ArticleIdDuplicatedException e){
            throw new ArticleCreateException("cannot create article");
        }
    }

    public long createComment(long articleId, String text) {
        CommentId commentId = commentRepository.generateId();
        Comment comment=new Comment(commentId, new ArticleId(articleId), text);
        try {
            commentRepository.create(comment);
            return comment.getId();
        }catch (CommentIdDuplicatedException | ArticleNotFoundException e){
            throw new CommentCreateException("cannot create comment");
        }
    }

    public void articleUpdate(long articleId, String name, Set<String> tags){
        Article article;
        try{
            article=articleRepository.findById(articleId);
        }
        catch (ArticleNotFoundException e){
            throw new ArticleUpdateException("cannot find article with id="+articleId);
        }
        articleRepository.update(article.withName(name).withTags(tags));
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

    public void deleteArticle(long articleId){
        Article article;
        try{
            article=articleRepository.findById(articleId);
        }
        catch (ArticleNotFoundException e){
            throw new ArticleDeleteException("cannot delete book with id="+articleId);
        }
        articleRepository.delete(articleId,commentRepository);
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
