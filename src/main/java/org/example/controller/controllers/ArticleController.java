package org.example.controller.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.service.ArticleService;
import org.example.article.Article;
import org.example.comment.Comment;
import org.example.controller.Controller;
import org.example.controller.request.ArticleCreateRequest;
import org.example.controller.request.ArticleUpdateRequest;
import org.example.controller.request.CommentCreateRequest;
import org.example.controller.request.CommentUpdateRequest;
import org.example.controller.response.*;
import org.example.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Service;
public class ArticleController implements Controller {
    private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);
    private final Service service;
    private final ArticleService articleService;
    private final ObjectMapper objectMapper;

    public ArticleController(Service service, ArticleService articleService, ObjectMapper objectMapper) {
        this.service = service;
        this.articleService = articleService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void initializeEndpoints() {
        createArticle();
        createComment();
        updateArticle();
        updateComment();
        deleteArticle();
        deleteComment();
        getArticle();
        getComment();
    }
    private void getArticle(){
        service.get("api/articles/:articleId",(Request request, Response response)->{
            response.type("aplication/json");
            long articleId=Long.parseLong(request.params("articleId"));
            try{
                Article article=articleService.findArticleById(articleId);
                response.status(201);
                LOG.debug("Article is got");
                return objectMapper.writeValueAsString(new ArticleGetResponse(article.getName(), article.getTags(), article.getComments()));
            }
            catch (ArticleNotFoundException e){
                LOG.warn("cannot find article", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
            }
        });
    }
    private void getComment(){
        service.get("api/comments/:commentId",(Request request, Response response)->{
            response.type("aplication/json");
            long commentId=Long.parseLong(request.params("commentId"));
            try {
                Comment comment=articleService.findCommentById(commentId);
                response.status(201);
                LOG.debug("Comment is got");
                return objectMapper.writeValueAsString(new CommentGetResponse(comment.getId(), comment.getArticleId().getValue(), comment.getText()));
            }
            catch (CommentNotFoundException e){
                LOG.warn("cannot find comment", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
            }
        });
    }
    private void createArticle(){
        service.post("/api/articles",(Request request, Response response) -> {
            response.type("application/json");
            String body=request.body();
            ArticleCreateRequest articleCreateRequest=objectMapper.readValue(body, ArticleCreateRequest.class);
            try{
                long articleId=articleService.createArticle(articleCreateRequest.getName(), articleCreateRequest.getTags());
                response.status(201);
                LOG.debug("Article is created");
                return objectMapper.writeValueAsString(new ArticleCreateResponse(articleId));
            }
            catch (ArticleCreateException e){
                LOG.warn("cannot create article", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
            }
        });
    }
    private void createComment(){
        service.post("/api/comments", (Request request, Response response) ->{
            response.type("application/json");
            String body=request.body();
            CommentCreateRequest commentCreateRequest=objectMapper.readValue(body, CommentCreateRequest.class);
            try{
                long commentId=articleService.createComment(commentCreateRequest.getArticleId(), commentCreateRequest.getText());
                response.status(201);
                LOG.debug("Comment is created");
                return objectMapper.writeValueAsString(new CommentCreateResponse(commentId));
            }
            catch (CommentCreateException e){
                LOG.warn("cannot create comment", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
            }
        });
    }
    private void updateArticle(){
        service.put("/api/articles/:articleId", (Request request, Response response)->{
            response.body("application/json");
            long articleId=Long.parseLong(request.params("articleId"));
            ArticleUpdateRequest articleUpdateRequest=objectMapper.readValue(request.body(), ArticleUpdateRequest.class);
            try{
                articleService.articleUpdate(articleId,articleUpdateRequest.getName(),articleUpdateRequest.getTags());
                response.status(201);
                LOG.debug("Article is updated");
                return objectMapper.writeValueAsString(new ArticleUpdateResponse(articleId));
            }catch (ArticleUpdateException e){
                LOG.warn("cannot update article", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
            }
        });
    }
    private void updateComment(){
        service.put("/api/comments/:commentId", (Request request, Response response)->{
            response.body("application/json");
            long commentId=Long.parseLong(request.params("commentId"));
            CommentUpdateRequest commentUpdateRequest=objectMapper.readValue(request.body(),CommentUpdateRequest.class);
            try{
                articleService.commentUpdate(commentId,commentUpdateRequest.getText());
                response.status(201);
                LOG.debug("Comment is updated");
                return objectMapper.writeValueAsString(new CommentUpdateResponse(commentId));
            }catch (CommentUpdateException e){
                LOG.warn("cannot update comment", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
            }
        });
    }
    private void deleteArticle(){
        service.delete("/api/articles/:articleId", (Request request, Response response)->{
            response.body("application/json");
            long articleId=Long.parseLong(request.params("articleId"));
            try{
                articleService.deleteArticle(articleId);
                response.status(201);
                LOG.debug("Article is deleted");
                return objectMapper.writeValueAsString(new ArticleDeleteResponse(articleId));
            }
            catch (ArticleDeleteException e){
                LOG.warn("cannot delete article", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
            }
        });
    }
    private void deleteComment(){
        service.delete("/api/comments/:commentId", (Request request, Response response)->{
            response.body("application/json");
            long commentId = Long.parseLong(request.params("commentId"));
            try{
                articleService.deleteComment(commentId);
                response.status(201);
                LOG.debug("Comment is deleted");
                return objectMapper.writeValueAsString(new CommentDeleteResponse(commentId));
            }
            catch (CommentDeleteException e){
                LOG.warn("cannot delete comment", e);
                response.status(400);
                return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
            }
        });
    }
}
