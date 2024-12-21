package org.example;

import org.example.article.Article;
import org.example.article.ArticleId;
import org.example.articlerepository.InMemoryArticleRepository;
import org.example.comment.Comment;
import org.example.comment.CommentId;
import org.example.commentrepository.InMemoryCommentRepository;
import org.example.exceptions.*;
import org.example.service.ArticleService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ArticleServiceTest {
    InMemoryArticleRepository articleRepository=new InMemoryArticleRepository();
    InMemoryCommentRepository commentRepository=new InMemoryCommentRepository(articleRepository);
    ArticleService articleService=new ArticleService(articleRepository,commentRepository);
    Article testArticle=new Article(new ArticleId(1), "123", Set.of("qwe"), new ArrayList<>());
    Comment testComment=new Comment(new CommentId(1),new ArticleId(1), "123");
    String testName = "123";
    Set<String> testTags=Set.of("qwe");
    @Test
    void shouldFindArticle(){
        articleService.createArticle(testName,testTags);
        assertEquals(testArticle,articleService.findArticleById(1));
    }
    @Test
    void shouldThrowArticleNotFoundExceptionInFindArticle(){
        assertThrows(ArticleNotFoundException.class,()->articleService.findArticleById(1));
    }
    @Test
    void shouldFindComment(){
        articleService.createArticle(testName,testTags);
        articleService.createComment(1,testComment.getText());
        assertEquals(articleService.findCommentById(1),testComment);
    }
    @Test
    void shouldThrowCommentNotFoundExceptionInFindComment(){
        assertThrows(CommentNotFoundException.class,()->articleService.findCommentById(1));
    }
    @Test
    void shouldCreateArticle(){
        articleService.createArticle(testName,testTags);
        assertEquals(testArticle,articleService.findArticleById(1));
    }
    @Test
    void shouldCreateComment(){
        articleService.createArticle(testName,testTags);
        articleService.createComment(1,testComment.getText());
        assertEquals(articleService.findCommentById(1),testComment);
    }
    @Test
    void shouldThrowCommentCreateException(){
        assertThrows(CommentCreateException.class,()->articleService.createComment(1,""));
    }
    @Test
    void shouldUpdateArticle(){
        articleService.createArticle(testName,testTags);
        Article newArticle=new Article(new ArticleId(1),"q",Set.of(),new ArrayList<>());
        articleService.articleUpdate(1,"q",Set.of());
        assertEquals(newArticle,articleService.findArticleById(1));
    }
    @Test
    void shouldThrowArticleUpdateException(){
        assertThrows(ArticleUpdateException.class,()->articleService.articleUpdate(1,"",Set.of()));
    }
    @Test
    void shouldUpdateComment(){
        articleService.createArticle(testName,testTags);
        articleService.createComment(1,testComment.getText());
        Comment newComment=new Comment(new CommentId(1),new ArticleId(1),"124");
        articleService.commentUpdate(1,"124");
        assertEquals(articleService.findCommentById(1),newComment);
        assertEquals(articleService.findArticleById(1).getComments(), List.of(newComment));
    }
    @Test
    void shouldThrowCommentUpdateExceptionInCommentUpdate(){
        assertThrows(CommentUpdateException.class,()->articleService.commentUpdate(1,"123"));
    }
    @Test
    void shouldDeleteArticle(){
        articleService.createArticle(testName,testTags);
        articleService.createComment(1,testComment.getText());
        articleService.deleteArticle(1);
        assertThrows(ArticleNotFoundException.class,()->articleService.findArticleById(1));
        assertThrows(CommentNotFoundException.class,()->articleService.findCommentById(1));
    }
    @Test
    void shouldThrowArticleDeleteException(){
        assertThrows(ArticleDeleteException.class,()->articleService.deleteArticle(1));
    }
    @Test
    void shouldDeleteComment(){
        articleService.createArticle(testName,testTags);
        articleService.createComment(1,testComment.getText());
        articleService.deleteComment(1);
        assertThrows(CommentNotFoundException.class,()->articleService.findCommentById(1));
        assertEquals(articleService.findArticleById(1).getComments().size(),0);
    }
    @Test
    void shouldThrowCommentDeleteException(){
        assertThrows(CommentDeleteException.class,()->articleService.deleteComment(1));
    }
}
