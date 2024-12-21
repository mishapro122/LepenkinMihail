package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.Application;
import org.example.articlerepository.InMemoryArticleRepository;
import org.example.commentrepository.InMemoryCommentRepository;
import org.example.controller.controllers.ArticleController;
import org.example.controller.controllers.ArticleFreemarkerController;
import org.example.service.ArticleService;
import templatefactory.TemplateFactory;
import spark.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {
    public static void main(String[] args) throws Exception {
        Service service = Service.ignite();
        ObjectMapper objectMapper = new ObjectMapper();
        InMemoryArticleRepository articleRepository = new InMemoryArticleRepository();
        InMemoryCommentRepository commentRepository = new InMemoryCommentRepository(articleRepository);
        ArticleService articleService = new ArticleService(articleRepository,commentRepository);
        Application application = new Application(List.of(new ArticleController(service,articleService,objectMapper), new ArticleFreemarkerController(service, articleService, TemplateFactory.freeMarkerEngine())));
        application.start();
        HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(
                                HttpRequest.BodyPublishers.ofString(
                                        """
                                                {"name": "qwerty", "tags": ["1"]}"""
                                )
                        ).uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(
                                HttpRequest.BodyPublishers.ofString(
                                        """
                                                {"name": "qwerty", "tags": ["2"]}"""
                                )
                        ).uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(
                                HttpRequest.BodyPublishers.ofString(
                                        """
                                                {"name": "qwerty", "tags": ["3"]}"""
                                )
                        ).uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(
                        """
                                {"articleId":1, "text":"1"}"""
                )).uri(URI.create("http://localhost:%d/api/comments".formatted(service.port()))).build(),HttpResponse.BodyHandlers.ofString(UTF_8));
    }
}
