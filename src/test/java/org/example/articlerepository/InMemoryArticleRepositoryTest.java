package org.example.articlerepository;

import org.example.article.Article;
import org.example.article.ArticleId;
import org.example.commentrepository.InMemoryCommentRepository;
import org.example.exceptions.ArticleIdDuplicatedException;
import org.example.exceptions.ArticleNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryArticleRepositoryTest {
    ArticleRepository articleRepository=new InMemoryArticleRepository();
    @Test
    void shouldFindArticle(){
        Article article=new Article(new ArticleId(1), "qwerty", Set.of("123"), new ArrayList<>());
        articleRepository.create(article);
        assertEquals(articleRepository.findById(1),article);
    }
    @Test
    void shouldThrowArticleNotFoundException(){
        assertThrows(ArticleNotFoundException.class, ()->articleRepository.findById(1));
    }
    @Test
    void shouldCreateArticle(){
        Article article=new Article(new ArticleId(1), "qwerty", Set.of("123"), new ArrayList<>());
        articleRepository.create(article);
        assertEquals(articleRepository.findById(1),article);
    }
    @Test
    void shouldThrowArticleIdDuplicatedException(){
        Article article=new Article(new ArticleId(1), "qwerty", Set.of("123"), new ArrayList<>());
        articleRepository.create(article);
        assertThrows(ArticleIdDuplicatedException.class, () -> articleRepository.create(article));
    }
    @Test
    void shouldUpdateArticle(){
        Article article=new Article(new ArticleId(1), "qwerty", Set.of("123"), new ArrayList<>());
        Article article1=new Article(new ArticleId(1),"qwe", Set.of("124"), new ArrayList<>());
        articleRepository.create(article);
        articleRepository.update(article1);
        assertEquals(article1,articleRepository.findById(1));
    }
    @Test
    void shouldThrowArticleNotFoundExceptionInUpdate(){
        Article article=new Article(new ArticleId(1), "qwerty", Set.of("123"), new ArrayList<>());
        Article article1=new Article(new ArticleId(2),"qwe", Set.of("124"), new ArrayList<>());
        articleRepository.create(article);
        assertThrows(ArticleNotFoundException.class,()->articleRepository.update(article1));
    }
    @Test
    void shouldDeleteArticle(){
        Article article=new Article(new ArticleId(1), "qwerty", Set.of("123"), new ArrayList<>());
        articleRepository.create(article);
        articleRepository.delete(article.getId(), new InMemoryCommentRepository((InMemoryArticleRepository) articleRepository));
        assertThrows(ArticleNotFoundException.class, ()->articleRepository.findById(article.getId()));
    }
    @Test
    void shouldThrowExceptionInDelete(){
        assertThrows(ArticleNotFoundException.class,()->articleRepository.delete(1,new InMemoryCommentRepository((InMemoryArticleRepository) articleRepository)));
    }
    @Test
    void shouldReturnAllArticles(){
        Article article=new Article(new ArticleId(1), "qwerty", Set.of("123"), new ArrayList<>());
        Article article1=new Article(new ArticleId(2),"qwe", Set.of("124"), new ArrayList<>());
        articleRepository.create(article);
        articleRepository.create(article1);
        assertEquals(articleRepository.findAll(), List.of(article,article1));
    }
}
