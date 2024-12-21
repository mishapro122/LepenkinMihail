package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.example.application.Application;
import org.example.articlerepository.InMemoryArticleRepository;
import org.example.commentrepository.InMemoryCommentRepository;
import org.example.controller.controllers.ArticleController;
import org.example.controller.controllers.ArticleFreemarkerController;
import org.example.controller.controllers.CommentController;
import org.example.service.ArticleService;
import org.example.service.CommentService;
import org.example.templatefactory.TemplateFactory;
import org.example.topicrepository.InMemoryTopicRepository;
import org.example.transactionmaneger.JdbiTransactionManager;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import spark.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {
    public static void main(String[] args) throws Exception {
        Config config = ConfigFactory.load();
        Flyway flyway =
                Flyway.configure()
                        .outOfOrder(true)
                        .locations("classpath:db/migrations")
                        .dataSource(config.getString("app.database.url"), config.getString("app.database.user"),
                                config.getString("app.database.password"))
                        .load();
        flyway.migrate();
        Jdbi jdbi = Jdbi.create(config.getString("app.database.url"), config.getString("app.database.user"),
                config.getString("app.database.password"));
        jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM article").execute());
        jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM topic").execute());
        jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM comment").execute());
        Service service = Service.ignite();
        ObjectMapper objectMapper = new ObjectMapper();
        InMemoryArticleRepository articleRepository = new InMemoryArticleRepository(jdbi);
        InMemoryCommentRepository commentRepository = new InMemoryCommentRepository(articleRepository,jdbi);
        InMemoryTopicRepository topicRepository = new InMemoryTopicRepository(articleRepository,jdbi);
        ArticleService articleService = new ArticleService(articleRepository,commentRepository, topicRepository, new JdbiTransactionManager(jdbi));
        CommentService commentService = new CommentService(articleRepository,commentRepository);
        Application application = new Application(List.of(new ArticleController(service,articleService,objectMapper), new CommentController(service, commentService, objectMapper),
                new ArticleFreemarkerController(service, articleService, TemplateFactory.freeMarkerEngine())));
        application.start();
        /*HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(
                                HttpRequest.BodyPublishers.ofString(
                                        """
                                                {"name": "qwerty1", "tags": ["1"]}"""
                                )
                        ).uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(
                                HttpRequest.BodyPublishers.ofString(
                                        """
                                                {"name": "qwerty2", "tags": ["2"]}"""
                                )
                        ).uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(
                                HttpRequest.BodyPublishers.ofString(
                                        """
                                                {"name": "qwerty3", "tags": ["3"]}"""
                                )
                        ).uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(
                        """
                                {"articleId":1, "text":"1"}"""
                )).uri(URI.create("http://localhost:%d/api/comments".formatted(service.port()))).build(),HttpResponse.BodyHandlers.ofString(UTF_8));
        HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.ofString(
                        """
                                {"name": "qwerty", "tags":["1","2"]}"""
                )).uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port()))).build(),
                HttpResponse.BodyHandlers.ofString(UTF_8)
        );*/
    }
}
