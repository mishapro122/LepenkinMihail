package org.example.controller.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.comment.Comment;
import org.example.controller.Controller;
import org.example.controller.request.CommentCreateRequest;
import org.example.controller.request.CommentUpdateRequest;
import org.example.controller.response.*;
import org.example.exceptions.CommentCreateException;
import org.example.exceptions.CommentDeleteException;
import org.example.exceptions.CommentNotFoundException;
import org.example.exceptions.CommentUpdateException;
import org.example.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Service;

public class CommentController implements Controller {
    private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);
    private final Service service;
    private final CommentService commentService;
    private final ObjectMapper objectMapper;

    public CommentController(Service service, CommentService commentService, ObjectMapper objectMapper) {
        this.service = service;
        this.commentService = commentService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void initializeEndpoints() {
        getComment();
        createComment();
        updateComment();
        deleteComment();
    }

    private void getComment(){
        service.get("api/comments/:commentId",(Request request, Response response)->{
            response.type("aplication/json");
            long commentId=Long.parseLong(request.params("commentId"));
            try {
                Comment comment=commentService.findCommentById(commentId);
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

    private void createComment(){
        service.post("/api/comments", (Request request, Response response) ->{
            response.type("application/json");
            String body=request.body();
            CommentCreateRequest commentCreateRequest=objectMapper.readValue(body, CommentCreateRequest.class);
            try{
                long commentId=commentService.createComment(commentCreateRequest.getArticleId(), commentCreateRequest.getText());
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

    private void updateComment(){
        service.put("/api/comments/:commentId", (Request request, Response response)->{
            response.body("application/json");
            long commentId=Long.parseLong(request.params("commentId"));
            CommentUpdateRequest commentUpdateRequest=objectMapper.readValue(request.body(),CommentUpdateRequest.class);
            try{
                commentService.commentUpdate(commentId,commentUpdateRequest.getText());
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

    private void deleteComment(){
        service.delete("/api/comments/:commentId", (Request request, Response response)->{
            response.body("application/json");
            long commentId = Long.parseLong(request.params("commentId"));
            try{
                commentService.deleteComment(commentId);
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
