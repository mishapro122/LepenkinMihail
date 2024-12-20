package org.example.articlerepository;

import org.example.article.Article;
import org.example.article.ArticleId;
import org.example.comment.Comment;
import org.example.commentrepository.CommentRepository;
import org.example.exceptions.ArticleIdDuplicatedException;
import org.example.exceptions.ArticleNotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryArticleRepository implements ArticleRepository {
    private final AtomicLong nextId = new AtomicLong(0);
    private final Map<Long,Article> articleMap = new ConcurrentHashMap<>();

    @Override
    public ArticleId generateId() {
        return new ArticleId(nextId.incrementAndGet());
    }

    @Override
    public List<Article> findAll() {
        return new ArrayList<>(articleMap.values());
    }

    @Override
    public Article findById(long articleId) {
        Article article=articleMap.get(articleId);

        if (article==null){
            throw new ArticleNotFoundException("Cannot find article by id=" + articleId);
        }
        return article;
    }

    @Override
    public synchronized void create(Article article) {
        if (articleMap.containsKey(article.getId())){
            throw new ArticleIdDuplicatedException("Article with the given id already exists: "+article.getId());
        }
        articleMap.put(article.getId(),article);
    }

    @Override
    public synchronized void update(Article article) {
        if (!articleMap.containsKey(article.getId())){
            throw new ArticleNotFoundException("Cannot find article by id=" + article.getId());
        }
        articleMap.put(article.getId(),article);
    }

    @Override
    public void delete(long articleId, CommentRepository commentRepository) {
        if (!articleMap.containsKey(articleId)){
            throw new ArticleNotFoundException("Cannot find article by id=" + articleId);
        }
        Article article=articleMap.get(articleId);
        List<Comment> comments=new ArrayList<>(List.copyOf(article.getComments()));
        for (Comment comment : comments){
            commentRepository.delete(comment.getId());
        }
        articleMap.remove(articleId);
    }
}
