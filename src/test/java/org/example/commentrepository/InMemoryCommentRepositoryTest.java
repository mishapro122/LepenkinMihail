package org.example.commentrepository;

import org.example.article.Article;
import org.example.article.ArticleId;
import org.example.articlerepository.InMemoryArticleRepository;
import org.example.comment.Comment;
import org.example.comment.CommentId;
import org.example.exceptions.ArticleNotFoundException;
import org.example.exceptions.CommentIdDuplicatedException;
import org.example.exceptions.CommentNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryCommentRepositoryTest {
    InMemoryArticleRepository articleRepository = new InMemoryArticleRepository();
    Article testArticle=new Article(new ArticleId(1), "123", Set.of("qwe"), new ArrayList<>());
    Comment testComment=new Comment(new CommentId(1),new ArticleId(1), "123");
    InMemoryCommentRepository commentRepository = new InMemoryCommentRepository(articleRepository);
     @Test
    void shouldFindComment(){
         articleRepository.create(testArticle);
         Comment comment=new Comment(new CommentId(1),new ArticleId(testArticle.getId()),"qwe");
         commentRepository.create(comment);
         assertEquals(commentRepository.findById(1),comment);
     }
     @Test
     void shouldThrowCommentNotFoundExceptionInFind(){
         assertThrows(CommentNotFoundException.class, ()->commentRepository.findById(1));
     }
     @Test
     void shouldCreateComment(){
         articleRepository.create(testArticle);
         Comment comment=new Comment(new CommentId(1),new ArticleId(testArticle.getId()),"qwe");
         commentRepository.create(comment);
         assertEquals(commentRepository.findById(1),comment);
     }
     @Test
    void shouldThrowCommentIdDuplicatedExceptionInCreate(){
         articleRepository.create(testArticle);
         Comment comment=new Comment(new CommentId(1),new ArticleId(testArticle.getId()),"qwe");
         commentRepository.create(comment);
         assertThrows(CommentIdDuplicatedException.class, ()->commentRepository.create(new Comment(new CommentId(1), new ArticleId(1), "qwe")));
     }
     @Test
     void shouldThrowArticleNotFoundExceptionInCreate(){
         assertThrows(ArticleNotFoundException.class,()->commentRepository.create(new Comment(new CommentId(1), new ArticleId(1), "qwe")));
     }
     @Test
    void shouldUpdateComment(){
         articleRepository.create(testArticle);
         Comment comment=new Comment(new CommentId(1),new ArticleId(testArticle.getId()),"qwe");
         Comment comment1=comment.withText("q");
         commentRepository.create(comment);
         commentRepository.update(comment1);
         assertEquals(comment1,commentRepository.findById(1));
     }
     @Test
     void shouldThrowCommentNotFoundException(){
         assertThrows(CommentNotFoundException.class, ()->commentRepository.update(testComment));
     }
     @Test
    void shouldDeleteComment(){
         articleRepository.create(testArticle);
         commentRepository.create(testComment);
         commentRepository.delete(testComment.getId());
         assertEquals(testArticle.getComments().size(),0);
         assertThrows(CommentNotFoundException.class,()->commentRepository.findById(testComment.getId()));
     }

     @Test
    void shouldThrowCommentNotFoundExceptionInDelete(){
         assertThrows(CommentNotFoundException.class,()->commentRepository.delete(1));
     }


}
