package org.example.topicrepository;

import org.example.article.Article;
import org.example.article.ArticleId;
import org.example.articlerepository.InMemoryArticleRepository;
import org.example.commentrepository.InMemoryCommentRepository;
import org.example.controller.controllers.ArticleController;
import org.example.exceptions.TopicIdDuplicatedException;
import org.example.exceptions.TopicNotFoundException;
import org.example.topic.Topic;
import org.example.topic.TopicId;
import org.example.topicrepository.InMemoryTopicRepository;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
class InMemoryTopicRepositoryTest {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryTopicRepositoryTest.class);
    @Container
    public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:latest");
    private static Jdbi jdbi;
    InMemoryArticleRepository articleRepository;
    InMemoryTopicRepository topicRepository;
    InMemoryCommentRepository commentRepository;
    Topic testTopic = new Topic(new ArticleId(1), new TopicId(1), "123");
    Article testArticle = new Article(new ArticleId(1), "123", Set.of(testTopic), new ArrayList<>(), false);
    Topic testTopic1 = new Topic(new ArticleId(1), new TopicId(2), "124");

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
        jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM comment"));
        articleRepository = new InMemoryArticleRepository(jdbi);
        topicRepository = new InMemoryTopicRepository(articleRepository, jdbi);
        commentRepository = new InMemoryCommentRepository(articleRepository, jdbi);
        commentRepository.generateId();
    }

    @Test
    void shouldFindTopicById() {
        articleRepository.create(testArticle, topicRepository);
        articleRepository.findById(1);
        assertEquals(topicRepository.findById(1), testTopic);
    }

    @Test
    void shouldThrowTopicNotFoundException() {
        assertThrows(TopicNotFoundException.class, () -> topicRepository.findById(1));
    }

    @Test
    void shouldCreateTopic() {
        articleRepository.create(testArticle, topicRepository);
        assertEquals(topicRepository.findById(1), testTopic);
    }

    @Test
    void shouldThrowTopicIdDuplicatedException() {
        articleRepository.create(testArticle, topicRepository);
        assertThrows(TopicIdDuplicatedException.class, () -> topicRepository.create(testTopic));
    }

    @Test
    void shouldUpdateTopics() {
        articleRepository.create(testArticle, topicRepository);
        topicRepository.generateId();
        assertEquals(topicRepository.updateTags(Set.of("124", "123"), testArticle.getTags(), new ArticleId(1)), Set.of(testTopic, testTopic1));
    }

    @Test
    void shouldDeleteTopics() {
        articleRepository.create(testArticle, topicRepository);
        topicRepository.delete(1);
        assertThrows(TopicNotFoundException.class, () -> topicRepository.findById(1));
    }
}
