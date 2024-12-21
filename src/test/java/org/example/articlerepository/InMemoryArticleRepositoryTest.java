package org.example.articlerepository;

import org.example.article.Article;
import org.example.article.ArticleId;
import org.example.articlerepository.InMemoryArticleRepository;
import org.example.commentrepository.InMemoryCommentRepository;
import org.example.exceptions.ArticleIdDuplicatedException;
import org.example.exceptions.ArticleNotFoundException;
import org.example.topic.Topic;
import org.example.topic.TopicId;
import org.example.topicrepository.InMemoryTopicRepository;
import org.example.topicrepository.TopicRepository;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
class InMemoryArticleRepositoryTest {
    @Container
    public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:13");
    private static Jdbi jdbi;
    InMemoryArticleRepository articleRepository;
    InMemoryTopicRepository topicRepository;

    @BeforeAll
    static void beforeAll() {
        String postgresJdbcUrl = POSTGRES.getJdbcUrl();
        Flyway flyway = Flyway.configure()
                .outOfOrder(true)
                .locations("classpath:db/migrations")
                .dataSource(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword())
                .load();
        flyway.migrate();
        jdbi = Jdbi.create(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword());
    }

    @BeforeEach
    void beforeEach() {
        jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM article").execute());
        jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM topic").execute());
        jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM comment").execute());
        articleRepository = new InMemoryArticleRepository(jdbi);
        topicRepository = new InMemoryTopicRepository(articleRepository, jdbi);
    }

    @Test
    void shouldFindArticle() {
        Article article =
                new Article(new ArticleId(1), "qwerty", Set.of(new Topic(new ArticleId(1), new TopicId(1), "123")), new ArrayList<>(), false);
        articleRepository.create(article, topicRepository);
        assertEquals(articleRepository.findById(1), article);
    }

    @Test
    void shouldThrowArticleNotFoundException() {
        assertThrows(ArticleNotFoundException.class, () -> articleRepository.findById(1));
    }

    @Test
    void shouldCreateArticle() {
        Article article = new Article(new ArticleId(1), "qwerty", Set.of(new Topic(new ArticleId(1), new TopicId(1), "123")), new ArrayList<>(), false);
        articleRepository.create(article, topicRepository);
        assertEquals(articleRepository.findById(1), article);
    }

    @Test
    void shouldThrowArticleIdDuplicatedException() {
        Article article = new Article(new ArticleId(1), "qwerty", Set.of(new Topic(new ArticleId(1), new TopicId(1), "123")), new ArrayList<>(), false);
        articleRepository.create(article, topicRepository);
        assertThrows(ArticleIdDuplicatedException.class, () -> articleRepository.create(article, topicRepository));
    }

    @Test
    void shouldUpdateArticle() {
        Article article = new Article(new ArticleId(1), "qwerty", Set.of(new Topic(new ArticleId(1), new TopicId(1), "123")), new ArrayList<>(), false);
        topicRepository.generateId();
        topicRepository.generateId();
        Article article1 = new Article(new ArticleId(1), "qwe", Set.of(new Topic(new ArticleId(1), new TopicId(1), "123")), new ArrayList<>(), false);
        articleRepository.create(article, topicRepository);
        articleRepository.update(article1);
        Article newArticle = articleRepository.findById(1);
        assertEquals(article1, newArticle);
    }

    @Test
    void shouldThrowArticleNotFoundExceptionInUpdate() {
        Article article = new Article(new ArticleId(1), "qwerty", Set.of(new Topic(new ArticleId(1), new TopicId(1), "123")), new ArrayList<>(), false);
        Article article1 = new Article(new ArticleId(2), "qwe", Set.of(new Topic(new ArticleId(2), new TopicId(1), "124")), new ArrayList<>(), false);
        articleRepository.create(article, topicRepository);
        assertThrows(ArticleNotFoundException.class, () -> articleRepository.update(article1));
    }

    @Test
    void shouldDeleteArticle() {
        Article article = new Article(new ArticleId(1), "qwerty", Set.of(new Topic(new ArticleId(1), new TopicId(1), "123")), new ArrayList<>(), false);
        articleRepository.create(article, topicRepository);
        articleRepository.delete(article.getId(), new InMemoryCommentRepository(articleRepository, jdbi), topicRepository);
        assertThrows(ArticleNotFoundException.class, () -> articleRepository.findById(article.getId()));
    }

    @Test
    void shouldThrowExceptionInDelete() {
        assertThrows(ArticleNotFoundException.class, () -> articleRepository.delete(1, new InMemoryCommentRepository(articleRepository, jdbi), topicRepository));
    }

    @Test
    void shouldReturnAllArticles() {
        Article article = new Article(new ArticleId(1), "qwerty", Set.of(new Topic(new ArticleId(1), new TopicId(1), "123")), new ArrayList<>(), false);
        Article article1 = new Article(new ArticleId(2), "qwe", Set.of(new Topic(new ArticleId(2), new TopicId(2), "124")), new ArrayList<>(), false);
        articleRepository.create(article, topicRepository);
        articleRepository.create(article1, topicRepository);
        assertEquals(new HashSet<>(articleRepository.findAll()), new HashSet<>(List.of(article, article1)));
    }
}
