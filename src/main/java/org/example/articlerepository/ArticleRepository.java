package org.example.articlerepository;

import org.example.article.Article;
import org.example.article.ArticleId;
import org.example.commentrepository.CommentRepository;
import org.example.topicrepository.TopicRepository;

import java.util.List;

public interface ArticleRepository {
    ArticleId generateId();

    List<Article> findAll();

    Article findById(long articleId);

    Article findByIdForUpdate(long articleId);

    void create(Article article, TopicRepository topicRepository);

    void update(Article article);

    void delete(long articleId, CommentRepository commentRepository, TopicRepository topicRepository);
}
