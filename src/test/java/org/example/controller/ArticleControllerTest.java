package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.Application;
import org.example.service.ArticleService;
import org.example.articlerepository.InMemoryArticleRepository;
import org.example.commentrepository.InMemoryCommentRepository;
import org.example.controller.controllers.ArticleController;
import org.example.controller.response.ArticleCreateResponse;
import org.example.controller.response.ArticleGetResponse;
import org.example.controller.response.CommentCreateResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArticleControllerTest {
    Service service;
    InMemoryArticleRepository articleRepository=new InMemoryArticleRepository();
    InMemoryCommentRepository commentRepository=new InMemoryCommentRepository(articleRepository);
    ArticleService articleService=new ArticleService(articleRepository,commentRepository);
    String testName="qwerty";
    Set<String> testTags=Set.of("1");
    Set<String> testNewTags=Set.of("2");
    ObjectMapper objectMapper=new ObjectMapper();
    Application application=new Application(List.of(new ArticleController(service,articleService,objectMapper)));
    @BeforeEach
    void beforeEach(){
        service=Service.ignite();
        objectMapper=new ObjectMapper();
        application=new Application(List.of(new ArticleController(service,articleService,objectMapper)));
    }

    @AfterEach
    void afterEach(){
        service.stop();
        service.awaitStop();
    }

    @Test
    void should201IfArticleIsCreated() throws Exception{
        //ObjectMapper objectMapper=new ObjectMapper();
        //Application application=new Application(List.of(new ArticleController(service,articleService,objectMapper)));
        application.start();
        service.awaitInitialization();
        HttpResponse<String> response= HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(
                        HttpRequest.BodyPublishers.ofString(
                                """
                                        {"name": "qwerty", "tags": ["1"]}"""
                        )
                ).uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        assertEquals(201, response.statusCode());
        ArticleCreateResponse articleCreateResponse=objectMapper.readValue(response.body(),ArticleCreateResponse.class);
        assertEquals(1, articleCreateResponse.getId());
    }

    @Test
    void should201IfCommentIsCreated() throws Exception{
        //ObjectMapper objectMapper=new ObjectMapper();
        //Application application=new Application(List.of(new ArticleController(service,articleService,objectMapper)));
        application.start();
        service.awaitInitialization();
        HttpResponse<String> response= HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(
                                HttpRequest.BodyPublishers.ofString(
                                        """
                                                {"name": "qwerty", "tags": ["1"]}"""
                                )
                        ).uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        ArticleCreateResponse articleCreateResponse=objectMapper.readValue(response.body(),ArticleCreateResponse.class);
        long articleId=articleCreateResponse.getId();
        HttpResponse<String> httpResponse=HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(
                        String.format("""
                                {"articleId":%d, "text":"1"}""",articleId)
                )).uri(URI.create("http://localhost:%d/api/comments".formatted(service.port()))).build(),HttpResponse.BodyHandlers.ofString(UTF_8));
        assertEquals(201,httpResponse.statusCode());
        CommentCreateResponse commentCreateResponse=objectMapper.readValue(httpResponse.body(),CommentCreateResponse.class);
        assertEquals(1,commentCreateResponse.getId());
    }
    @Test
    void should400IfCommentIsNotCreated() throws Exception{
        //ObjectMapper objectMapper=new ObjectMapper();
        //Application application=new Application(List.of(new ArticleController(service,articleService,objectMapper)));
        application.start();
        service.awaitInitialization();
        HttpResponse<String> httpResponse=HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(
                        """
                                {"articleId":1, "text":"1"}"""
                )).uri(URI.create("http://localhost:%d/api/comments".formatted(service.port()))).build(),HttpResponse.BodyHandlers.ofString(UTF_8));
        assertEquals(400,httpResponse.statusCode());
    }
    @Test
    void should201IfArticleIsGot() throws Exception{
        //ObjectMapper objectMapper=new ObjectMapper();
        //Application application=new Application(List.of(new ArticleController(service,articleService,objectMapper)));
        application.start();
        service.awaitInitialization();
        HttpResponse<String> response= HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(
                                HttpRequest.BodyPublishers.ofString(
                                        """
                                                {"name": "qwerty", "tags": ["1"]}"""
                                )
                        ).uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        HttpResponse<String> httpResponse=HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        assertEquals(201,httpResponse.statusCode());
        ArticleGetResponse articleGetResponse=objectMapper.readValue(httpResponse.body(),ArticleGetResponse.class);
        ArticleGetResponse checkArticleGetResponse = new ArticleGetResponse(testName, testTags, new ArrayList<>());
        assertEquals(articleGetResponse,checkArticleGetResponse);
    }
    @Test
    void should400IfArticleIsNotGot() throws Exception{
        //ObjectMapper objectMapper=new ObjectMapper();
        //Application application=new Application(List.of(new ArticleController(service,articleService,objectMapper)));
        application.start();
        service.awaitInitialization();
        HttpResponse<String> httpResponse=HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        assertEquals(httpResponse.statusCode(),400);
    }
    @Test
    void should201IfArticleIsUpdated() throws Exception{
        //ObjectMapper objectMapper=new ObjectMapper();
        //Application application=new Application(List.of(new ArticleController(service,articleService,objectMapper)));
        application.start();
        service.awaitInitialization();
        HttpResponse<String> response= HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(
                                HttpRequest.BodyPublishers.ofString(
                                        """
                                                {"name": "qwerty", "tags": ["1"]}"""
                                )
                        ).uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        HttpResponse<String> httpResponse=HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.ofString(
                        """
                                {"name": "qwerty", "tags":["2"]}"""
                )).uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port()))).build(),
                HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        assertEquals(201,httpResponse.statusCode());
        httpResponse=HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        ArticleGetResponse checkArticleGetResponse = new ArticleGetResponse(testName, testNewTags, new ArrayList<>());
        ArticleGetResponse articleGetResponse=objectMapper.readValue(httpResponse.body(),ArticleGetResponse.class);
        assertEquals(articleGetResponse,checkArticleGetResponse);
    }
    @Test
    void should400IfArticleIsNotUpdated() throws Exception{
        application.start();
        service.awaitInitialization();
        HttpResponse<String> httpResponse=HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.ofString(
                        """
                                {"name": "qwerty", "tags":["2"]}"""
                )).uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port()))).build(),
                HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        assertEquals(400,httpResponse.statusCode());
    }
    @Test
    void should201IfArticleIsDeleted() throws Exception{
        application.start();
        service.awaitInitialization();
        HttpResponse<String> response= HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(
                                HttpRequest.BodyPublishers.ofString(
                                        """
                                                {"name": "qwerty", "tags": ["1"]}"""
                                )
                        ).uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        ArticleCreateResponse articleCreateResponse=objectMapper.readValue(response.body(),ArticleCreateResponse.class);
        long articleId=articleCreateResponse.getId();
        HttpResponse<String> httpResponse=HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(
                        String.format("""
                                {"articleId":%d, "text":"1"}""",articleId)
                )).uri(URI.create("http://localhost:%d/api/comments".formatted(service.port()))).build(),HttpResponse.BodyHandlers.ofString(UTF_8));
        httpResponse=HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:%d/api/comments/1".formatted(service.port()))).build(),
                HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        assertEquals(201,httpResponse.statusCode());
        httpResponse=HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().DELETE().uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        assertEquals(201,httpResponse.statusCode());
        httpResponse=HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:%d/api/comments/1".formatted(service.port()))).build(),
                HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        assertEquals(400,httpResponse.statusCode());
    }
}
