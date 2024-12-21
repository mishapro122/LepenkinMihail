package org.example.service;

import org.example.article.Article;
import org.example.article.ArticleId;
import org.example.articlerepository.InMemoryArticleRepository;
import org.example.comment.Comment;
import org.example.comment.CommentId;
import org.example.commentrepository.InMemoryCommentRepository;
import org.example.exceptions.ArticleDeleteException;
import org.example.exceptions.ArticleNotFoundException;
import org.example.exceptions.ArticleUpdateException;
import org.example.exceptions.CommentNotFoundException;
import org.example.service.ArticleService;
import org.example.service.CommentService;
import org.example.topic.Topic;
import org.example.topic.TopicId;
import org.example.topicrepository.InMemoryTopicRepository;
import org.example.transactionmaneger.JdbiTransactionManager;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
class ArticleServiceTest {
    @Container
    public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:13");
    private static Jdbi jdbi;
    InMemoryArticleRepository articleRepository;
    InMemoryCommentRepository commentRepository;
    InMemoryTopicRepository topicRepository;
    ArticleService articleService;
    CommentService commentService;
    Article testArticle = new Article(new ArticleId(1), "123", Set.of(new Topic(new ArticleId(1), new TopicId(1), "qwe")), new ArrayList<>(), false);
    Comment testComment = new Comment(new CommentId(1), new ArticleId(1), "123");
    String testName = "123";
    Set<String> testTags = Set.of("qwe");

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
        commentRepository = new InMemoryCommentRepository(articleRepository, jdbi);
        articleService = new ArticleService(articleRepository, commentRepository, topicRepository, new JdbiTransactionManager(jdbi));
        commentService = new CommentService(articleRepository, commentRepository);
    }

    @Test
    void shouldFindArticle() {
        articleService.createArticle(testName, testTags);
        assertEquals(testArticle, articleService.findArticleById(1));
    }

    @Test
    void shouldThrowArticleNotFoundExceptionInFindArticle() {
        assertThrows(ArticleNotFoundException.class, () -> articleService.findArticleById(1));
    }

    @Test
    void shouldCreateArticle() {
        articleService.createArticle(testName, testTags);
        assertEquals(testArticle, articleService.findArticleById(1));
    }

    @Test
    void shouldCreateArticles() {
        HashSet<Map<String, Long>> set = new HashSet<>(Set.of(Map.of("1", 1L), Map.of("2", 2L), Map.of("3", 3L), Map.of("4", 4L)));
        List<Map<String, Set<String>>> mapList = new ArrayList<>(List.of(Map.of("1", Set.of("1")), Map.of("2", Set.of("2")), Map.of("3", Set.of("3")), Map.of("4", Set.of("4"))));
        Set<Map<String, Long>> resultSet = articleService.createArticles(mapList);
        assertEquals(set, resultSet);
    }

    @Test
    void shouldUpdateArticle() {
        articleService.createArticle(testName, testTags);
        Article newArticle = new Article(new ArticleId(1), "q", Set.of(), new ArrayList<>(), false);
        articleService.articleUpdate(1, "q", Set.of());
        assertEquals(newArticle, articleService.findArticleById(1));
    }

    @Test
    void shouldThrowArticleUpdateException() {
        assertThrows(ArticleUpdateException.class, () -> articleService.articleUpdate(1, "", Set.of()));
    }

    @Test
    void shouldDeleteArticle() {
        articleService.createArticle(testName, testTags);
        commentService.createComment(1, testComment.getText());
        articleService.deleteArticle(1);
        assertThrows(ArticleNotFoundException.class, () -> articleService.findArticleById(1));
        assertThrows(CommentNotFoundException.class, () -> commentService.findCommentById(1));
    }

    @Test
    void shouldThrowArticleDeleteException() {
        assertThrows(ArticleDeleteException.class, () -> articleService.deleteArticle(1));
    }
}
