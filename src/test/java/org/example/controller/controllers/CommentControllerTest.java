package org.example.controller.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.Application;
import org.example.articlerepository.InMemoryArticleRepository;
import org.example.commentrepository.InMemoryCommentRepository;
import org.example.controller.controllers.ArticleController;
import org.example.controller.controllers.CommentController;
import org.example.controller.response.ArticleCreateResponse;
import org.example.controller.response.CommentCreateResponse;
import org.example.service.ArticleService;
import org.example.service.CommentService;
import org.example.topicrepository.InMemoryTopicRepository;
import org.example.transactionmaneger.JdbiTransactionManager;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import spark.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class CommentControllerTest {
    @Container
    public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:13");
    private static Jdbi jdbi;
    InMemoryArticleRepository articleRepository;
    InMemoryCommentRepository commentRepository;
    InMemoryTopicRepository topicRepository;
    ArticleService articleService;
    CommentService commentService;
    Service service;
    ObjectMapper objectMapper;
    Application application;

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
        service = Service.ignite();
        objectMapper = new ObjectMapper();
        application = new Application(List.of(new ArticleController(service, articleService, objectMapper), new CommentController(service, commentService, objectMapper)));
    }

    @AfterEach
    void afterEach() {
        service.stop();
        service.awaitStop();
    }

    @Test
    void should201IfCommentIsCreated() throws Exception {
        application.start();
        service.awaitInitialization();
        HttpResponse<String> response = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(
                                HttpRequest.BodyPublishers.ofString(
                                        """
                                                {"name": "qwerty", "tags": ["1"]}"""
                                )
                        ).uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        ArticleCreateResponse articleCreateResponse = objectMapper.readValue(response.body(), ArticleCreateResponse.class);
        long articleId = articleCreateResponse.getId();
        HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(
                        String.format("""
                                {"articleId":%d, "text":"1"}""", articleId)
                )).uri(URI.create("http://localhost:%d/api/comments".formatted(service.port()))).build(), HttpResponse.BodyHandlers.ofString(UTF_8));
        assertEquals(201, httpResponse.statusCode());
        CommentCreateResponse commentCreateResponse = objectMapper.readValue(httpResponse.body(), CommentCreateResponse.class);
        assertEquals(1, commentCreateResponse.getId());
    }

    @Test
    void should400IfCommentIsNotCreated() throws Exception {
        application.start();
        service.awaitInitialization();
        HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(
                        """
                                {"articleId":1, "text":"1"}"""
                )).uri(URI.create("http://localhost:%d/api/comments".formatted(service.port()))).build(), HttpResponse.BodyHandlers.ofString(UTF_8));
        assertEquals(400, httpResponse.statusCode());
    }
}
