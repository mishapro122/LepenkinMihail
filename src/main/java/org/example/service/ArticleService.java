package org.example.service;

import org.example.article.Article;
import org.example.article.ArticleId;
import org.example.articlerepository.ArticleRepository;
import org.example.commentrepository.CommentRepository;
import org.example.exceptions.*;
import org.example.topic.Topic;
import org.example.topicrepository.TopicRepository;
import org.example.transactionmaneger.TransactionManager;

import java.util.*;

public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final TopicRepository topicRepository;
    private final TransactionManager transactionManager;
    public ArticleService(ArticleRepository articleRepository, CommentRepository commentRepository, TopicRepository topicRepository, TransactionManager transactionManager) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.topicRepository = topicRepository;
        this.transactionManager = transactionManager;
    }
    public List<Article> findAll(){
        return articleRepository.findAll();
    }
    public Article findArticleById(long id){
        try{
            return articleRepository.findById(id);
        } catch (ArticleNotFoundException e) {
            throw new ArticleNotFoundException("cannot find article with id="+id);
        }
    }

    /*public Comment findCommentById(long id){
        try{
            return commentRepository.findById(id);
        }
        catch (CommentNotFoundException e){
            throw new CommentNotFoundException("cannot find comment with id="+id);
        }
    }*/

    public long createArticle(String name, Set<String> tags){
        ArticleId articleId = articleRepository.generateId();
        Set<Topic> topics = new HashSet<>();
        for (String tag : tags){
            Topic topic = new Topic(articleId, topicRepository.generateId(), tag);
            topics.add(topic);
        }

        Article article=new Article(articleId, name, topics, new ArrayList<>(), false);
        try{
            articleRepository.create(article, topicRepository);
            return article.getId();
        }
        catch (ArticleIdDuplicatedException e){
            throw new ArticleCreateException("cannot create article");
        }
    }

    public Set<Map<String,Long>> createArticles(List<Map<String,Set<String>>> articlesMap){
        return transactionManager.inTransaction(() ->{
            Set<Map<String, Long>> resultSet = new HashSet<>();
            for (Map<String, Set<String>> map : articlesMap){
                for (Map.Entry<String, Set<String>> entry : map.entrySet()){
                    Article article = new Article(articleRepository.generateId(), entry.getKey(),Set.of(),new ArrayList<>(),false);
                    try {
                        articleRepository.create(article,topicRepository);
                    }
                    catch (ArticleIdDuplicatedException e){
                        throw new ArticleCreateException("cannot create article");
                    }
                    resultSet.add(Map.of(article.getName(),article.getId()));
                }
                /*Article article = new Article(articleRepository.generateId(), entry.getKey(),Set.of(),new ArrayList<>(),false);
                try {
                    articleRepository.create(article,topicRepository);
                }
                catch (ArticleIdDuplicatedException e){
                    throw new ArticleCreateException("cannot create article");
                }
                resultSet.add(Map.of(article.getName(),article.getId()));*/
            }
            return resultSet;
        });
    }



    /*public long createComment(long articleId, String text) {
        CommentId commentId = commentRepository.generateId();
        Comment comment=new Comment(commentId, new ArticleId(articleId), text);
        try {
            commentRepository.create(comment);
            return comment.getId();
        }catch (CommentIdDuplicatedException | ArticleNotFoundException e){
            throw new CommentCreateException("cannot create comment");
        }
    }*/

    public void articleUpdate(long articleId, String name, Set<String> tags){
        Article article;
        try{
            article=articleRepository.findById(articleId);
        }
        catch (ArticleNotFoundException e){
            throw new ArticleUpdateException("cannot find article with id="+articleId);
        }
        Set<Topic> newTopics = topicRepository.updateTags(tags, article.getTags(),new ArticleId(article.getId()));
        articleRepository.update(article.withName(name).withTags(newTopics));
    }
    /*public void commentUpdate(long commentId, String text){
        Comment newComment;
        try{
            newComment =commentRepository.findById(commentId);
        }
        catch (CommentNotFoundException e){
            throw new CommentUpdateException("cannot find comment with id="+commentId);
        }
        commentRepository.update(newComment.withText(text));
    }*/

    public void deleteArticle(long articleId){
        Article article;
        try{
            article=articleRepository.findById(articleId);
        }
        catch (ArticleNotFoundException e){
            throw new ArticleDeleteException("cannot delete book with id="+articleId);
        }
        articleRepository.delete(articleId,commentRepository,topicRepository);
    }

    /*public void deleteComment(long commentId){
        Comment comment;
        try {
            comment=commentRepository.findById(commentId);
        }catch (CommentNotFoundException e){
            throw new CommentDeleteException("cannot delete comment with id="+commentId);
        }
        commentRepository.delete(commentId);
    }*/
}
