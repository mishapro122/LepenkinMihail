package org.example.articlerepository;

import org.example.article.Article;
import org.example.article.ArticleId;
import org.example.comment.Comment;
import org.example.comment.CommentId;
import org.example.commentrepository.CommentRepository;
import org.example.exceptions.ArticleIdDuplicatedException;
import org.example.exceptions.ArticleNotFoundException;
import org.example.topic.Topic;
import org.example.topic.TopicId;
import org.example.topicrepository.TopicRepository;
import org.flywaydb.core.internal.jdbc.Result;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultIterable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class InMemoryArticleRepository implements ArticleRepository {
    private final AtomicLong nextId = new AtomicLong(0);
    private final Map<Long,Article> articleMap = new ConcurrentHashMap<>();
    private final Jdbi jdbi;

    public InMemoryArticleRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public ArticleId generateId() {
        return new ArticleId(nextId.incrementAndGet());
    }

    @Override
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();
        try (Handle handle = jdbi.open()){
           Map<Long, Article> articlesMap= handle.createQuery("SELECT * FROM article")
                    .map((rs, ctx) -> new Article(
                            new ArticleId(rs.getLong("id")),
                            rs.getString("name"),
                            Set.of(),
                            new ArrayList<>(),
                            rs.getBoolean("trending")
                    ))
                    .list()
        .stream()
                    .collect(Collectors.toMap(Article::getId, article -> article));
            List<Topic> topics = handle.createQuery(
                            "SELECT t.topic_id, t.text, t.article_id " +
                                    "FROM topic t " +
                                    "JOIN article a ON t.article_id = a.id")
                    .map((rs, ctx) -> new Topic(
                            new ArticleId(rs.getLong("article_id")),
                            new TopicId(rs.getLong("topic_id")),
                            rs.getString("text")
                    ))
                    .list();
            Map<Long,Set<Topic>> topicMap = topics.stream()
                    .collect(Collectors.groupingBy(
                            topic -> topic.getArticleId().getValue(),
                            Collectors.mapping(topic -> topic, Collectors.toSet()) 
                    ));
            List<Comment> commentList = handle.createQuery("SELECT c.comment_id, c.text, c.article_id " +
                    "FROM comment c " +
                    "JOIN article a ON c.article_id = a.id").map((rs, ctx) -> new Comment(new CommentId(rs.getLong("comment_id")), new ArticleId(rs.getLong("article_id")), rs.getString("text") )).list();
            Map<Long, List<Comment>> commentMap = commentList.stream().collect(Collectors.groupingBy(comment -> comment.getArticleId().getValue(),
                    Collectors.mapping(comment -> comment, Collectors.toList())));
            for (Map.Entry<Long,Article> entry: articlesMap.entrySet()){
                Article article = entry.getValue();
                Long articleId = entry.getKey();
                articles.add(article.withComments(commentMap.get(articleId)).withTags(topicMap.get(articleId)));
            }
        }

        return articles;
    }


    @Override
    public Article findById(long articleId) {
        Article article;
        try(Handle handle = jdbi.open()){
            ResultIterable<Article> result = handle.createQuery("SELECT id, name, trending FROM article WHERE id = :id").bind("id", articleId).map((rs, ctx) -> new Article(new ArticleId(articleId), rs.getString("name"), Set.of(), new ArrayList<>(), rs.getBoolean("trending")));
            try{
                article = result.first();
            } catch (Exception e) {
                throw new ArticleNotFoundException("Cannot find article by id=" + articleId);
            }
            Set<Topic> topics = handle.createQuery("SELECT t.topic_id, t.text, t.article_id " +
                    "FROM topic t " +
                    "WHERE t.article_id = :article_id").bind("article_id", articleId).map((rs, ctx) -> new Topic(
                    new ArticleId(rs.getLong("article_id")),
                    new TopicId(rs.getLong("topic_id")),
                    rs.getString("text")
            )).set();
            List<Comment> comments = handle.createQuery("SELECT c.comment_id, c.text, c.article_id " +
                    "FROM comment c " +
                    "WHERE c.article_id = :article_id").bind("article_id", articleId).map((rs, ctx) -> new Comment(new CommentId(rs.getLong("comment_id")), new ArticleId(articleId), rs.getString("text"))).list();
            article = article.withTags(topics).withComments(comments);

        }
        return article;
        /*Article article=articleMap.get(articleId);

        if (article==null){
            throw new ArticleNotFoundException("Cannot find article by id=" + articleId);
        }
        return article;*/
    }
    @Override
    public Article findByIdForUpdate(long articleId){
        Article article;
        try(Handle handle = jdbi.open()){
            ResultIterable<Article> result = handle.createQuery("SELECT id, name, trending FROM article WHERE id = :id FOR UPDATE").bind("id", articleId).map((rs, ctx) -> new Article(new ArticleId(articleId), rs.getString("name"), Set.of(), new ArrayList<>(), rs.getBoolean("trending")));
            try{
                article = result.first();
            } catch (Exception e) {
                throw new ArticleNotFoundException("Cannot find article by id=" + articleId);
            }
            Set<Topic> topics = handle.createQuery("SELECT t.topic_id, t.text, t.article_id " +
                    "FROM topic t " +
                    "WHERE t.article_id = :article_id").bind("article_id", articleId).map((rs, ctx) -> new Topic(
                    new ArticleId(rs.getLong("article_id")),
                    new TopicId(rs.getLong("topic_id")),
                    rs.getString("text")
            )).set();
            List<Comment> comments = handle.createQuery("SELECT c.comment_id, c.text, c.article_id " +
                    "FROM comment c " +
                    "WHERE c.article_id = :article_id").bind("article_id", articleId).map((rs, ctx) -> new Comment(new CommentId(rs.getLong("comment_id")), new ArticleId(articleId), rs.getString("text"))).list();
            article = article.withTags(topics).withComments(comments);
        }
        return article;
    }

    @Override
    public synchronized void create(Article article, TopicRepository topicRepository) {
        try(Handle handle = jdbi.open()){
            Set<String> set = handle.createQuery("SELECT id, name FROM article WHERE id = :id").bind("id",article.getId()).map((rs, ctx)-> rs.getString("name")).set();
            if (!set.isEmpty()){
                throw new ArticleIdDuplicatedException("Article with the given id already exists: "+article.getId());
            }
            handle.createUpdate("INSERT INTO article (id, name, trending) VALUES (:id, :name, :trending)")
                    .bind("id", article.getId())
                    .bind("name", article.getName())
                    .bind("trending", article.isTrending())
                    .execute();
            for (Topic topic : article.getTags()){
                topicRepository.create(topic);
            }
        }
        /*if (articleMap.containsKey(article.getId())){
            throw new ArticleIdDuplicatedException("Article with the given id already exists: "+article.getId());
        }
        articleMap.put(article.getId(),article);
        for (Topic topic : article.getTags()){
            topicRepository.create(topic);
        }*/
    }

    @Override
    public synchronized void update(Article article) {
        try(Handle handle = jdbi.open()){
            Set<String> set = handle.createQuery("SELECT id, name FROM article WHERE id = :id").bind("id",article.getId()).map((rs, ctx)-> rs.getString("name")).set();
            if (set.isEmpty()){
                throw new ArticleNotFoundException("Cannot find article by id=" + article.getId());
            }
            handle.createUpdate("UPDATE article SET name=:name, trending=:trending WHERE id=:id")
                    .bind("name",article.getName())
                    .bind("trending",article.isTrending())
                    .bind("id",article.getId())
                    .execute();
        }
        /*if (!articleMap.containsKey(article.getId())){
            throw new ArticleNotFoundException("Cannot find article by id=" + article.getId());
        }
        articleMap.put(article.getId(),article);*/
    }

    @Override
    public void delete(long articleId, CommentRepository commentRepository, TopicRepository topicRepository) {
        try(Handle handle = jdbi.open()){
            Set<String> set = handle.createQuery("SELECT id, name FROM article WHERE id = :id").bind("id",articleId).map((rs, ctx)-> rs.getString("name")).set();
            if (set.isEmpty()){
                throw new ArticleNotFoundException("Cannot find article by id=" + articleId);
            }
            Article article = findById(articleId);
            List<Comment> comments=new ArrayList<>(List.copyOf(article.getComments()));
            Set<Topic> topics = article.getTags();
            for (Comment comment : comments){
                commentRepository.delete(comment.getId());
            }
            for (Topic topic : topics){
                topicRepository.delete(topic.getTopicId());
            }
            handle.createUpdate("DELETE FROM article WHERE id=:id")
                    .bind("id",articleId)
                    .execute();
        }
        /*if (!articleMap.containsKey(articleId)){
            throw new ArticleNotFoundException("Cannot find article by id=" + articleId);
        }
        Article article=articleMap.get(articleId);
        List<Comment> comments=new ArrayList<>(List.copyOf(article.getComments()));
        for (Comment comment : comments){
            commentRepository.delete(comment.getId());
        }
        Set<Topic> topics = article.getTags();
        for (Topic topic : topics){
            topicRepository.delete(topic.getTopicId());
        }
        articleMap.remove(articleId);*/
    }
}
