package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.application.Application;
import org.example.article.ArticleId;
import org.example.articlerepository.InMemoryArticleRepository;
import org.example.comment.Comment;
import org.example.comment.CommentId;
import org.example.commentrepository.InMemoryCommentRepository;
import org.example.controller.controllers.ArticleController;
import org.example.controller.response.ArticleCreateResponse;
import org.example.controller.response.ArticleGetResponse;
import org.example.controller.response.CommentCreateResponse;
import org.example.service.ArticleService;
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

class ApplicationTest {
    Service service;
    InMemoryArticleRepository articleRepository=new InMemoryArticleRepository();
    InMemoryCommentRepository commentRepository=new InMemoryCommentRepository(articleRepository);
    ArticleService articleService=new ArticleService(articleRepository,commentRepository);
    String testName="qwerty";
    String testText="1";
    Set<String> testTags=Set.of("1");
    Set<String> testNewTags=Set.of("2");
    //ObjectMapper objectMapper=new ObjectMapper();
    //Application application=new Application(List.of(new ArticleController(service,articleService,objectMapper)));
    @BeforeEach
    void beforeEach(){
        service=Service.ignite();
        //objectMapper=new ObjectMapper();
        //application=new Application(List.of(new ArticleController(service,articleService,objectMapper)));
    }
    @AfterEach
    void afterEach(){
        service.stop();
        service.awaitStop();
    }
    @Test
    void E2ETest() throws Exception{
        ObjectMapper objectMapper=new ObjectMapper();
        Application application=new Application(List.of(new ArticleController(service,articleService,objectMapper)));
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
        assertEquals(201,response.statusCode());
        ArticleCreateResponse articleCreateResponse=objectMapper.readValue(response.body(),ArticleCreateResponse.class);
        long articleId=articleCreateResponse.getId();
        response=HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(
                        String.format("""
                                {"articleId":%d, "text":"1"}""",articleId)
                )).uri(URI.create("http://localhost:%d/api/comments".formatted(service.port()))).build(),HttpResponse.BodyHandlers.ofString(UTF_8));
        assertEquals(201, response.statusCode());
        CommentCreateResponse commentCreateResponse=objectMapper.readValue(response.body(),CommentCreateResponse.class);
        long commentId=commentCreateResponse.getId();
        response = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.ofString(
                        """
                                {"name": "qwerty", "tags":["2"]}"""
                )).uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port()))).build(),
                HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        assertEquals(response.statusCode(),201);
        HttpResponse<String> httpResponse=HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        assertEquals(httpResponse.statusCode(),201);
        ArticleGetResponse articleGetResponse=objectMapper.readValue(httpResponse.body(), ArticleGetResponse.class);
        ArticleGetResponse checkArticleGetResponse=new ArticleGetResponse(testName,testNewTags, new ArrayList<>(List.of(new Comment(new CommentId(commentId),new ArticleId(articleId),testText))));
        assertEquals(checkArticleGetResponse,articleGetResponse);
        response = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().DELETE().uri(URI.create("http://localhost:%d/api/comments/1".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        assertEquals(response.statusCode(),201);
        httpResponse=HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        articleGetResponse = objectMapper.readValue(httpResponse.body(), ArticleGetResponse.class);
        checkArticleGetResponse=new ArticleGetResponse(testName, testNewTags, new ArrayList<>());
        assertEquals(articleGetResponse,checkArticleGetResponse);
        response = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().DELETE().uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port())))
                        .build(), HttpResponse.BodyHandlers.ofString(UTF_8)
        );
        assertEquals(201,response.statusCode());
    }
}
