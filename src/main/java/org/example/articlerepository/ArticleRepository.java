package org.example.articlerepository;

import org.example.article.Article;
import org.example.article.ArticleId;
import org.example.commentrepository.CommentRepository;

import java.util.List;

public interface ArticleRepository {
    ArticleId generateId();

    List<Article> findAll();

    Article findById(long articleId);

    void create(Article article);

    void update(Article article);

    void delete(long articleId, CommentRepository commentRepository);
}
