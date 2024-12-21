package org.example.commentrepository;

import org.example.article.Article;
import org.example.article.ArticleId;
import org.example.articlerepository.InMemoryArticleRepository;
import org.example.comment.Comment;
import org.example.comment.CommentId;
import org.example.exceptions.ArticleNotFoundException;
import org.example.exceptions.CommentIdDuplicatedException;
import org.example.exceptions.CommentNotFoundException;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultIterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryCommentRepository implements CommentRepository {
    private final AtomicLong nextId = new AtomicLong(0);
    private final Map<Long, Comment> commentMap = new ConcurrentHashMap<>();
    private final InMemoryArticleRepository articleRepository;
    private final Jdbi jdbi;

    public InMemoryCommentRepository(InMemoryArticleRepository articleRepository, Jdbi jdbi) {
        this.articleRepository = articleRepository;
        this.jdbi = jdbi;
    }

    @Override
    public CommentId generateId() {
        return new CommentId(nextId.incrementAndGet());
    }

    @Override
    public List<Comment> findAll() {
        return new ArrayList<>(commentMap.values());
    }

    @Override
    public Comment findById(long commentId) {
        Comment comment;
        try(Handle handle = jdbi.open()){
            ResultIterable<Comment> result = handle.createQuery("SELECT comment_id, text, article_id FROM comment WHERE comment_id=:comment_id")
                    .bind("comment_id", commentId).map((rs,ctx) -> new Comment(new CommentId(rs.getLong("comment_id")), new ArticleId(rs.getLong("article_id")), rs.getString("text")));
            try{
                comment = result.first();
            } catch (Exception e) {
                throw new CommentNotFoundException("Cannot find comment by id=" + commentId);
            }
        }
        return comment;
        /*Comment comment=commentMap.get(commentId);

        if (comment==null){
            throw new CommentNotFoundException("Cannot find comment by id=" + commentId);
        }
        return comment;*/
    }

    @Override
    public synchronized void create(Comment comment) {
        Article article;
        try{
            article = articleRepository.findById(comment.getArticleId().getValue());
        }
        catch (ArticleNotFoundException e){
            throw e;
        }
        try(Handle handle = jdbi.open()){
            Set<String> set = handle.createQuery("SELECT text,comment_id FROM comment WHERE comment_id=:comment_id").bind("comment_id",comment.getId())
                    .map((rs, ctx) -> rs.getString("text")).set();
            if (!set.isEmpty()){
                throw new CommentIdDuplicatedException("Comment with the given id already exists: "+comment.getId());
            }
            handle.createUpdate("INSERT INTO comment (comment_id, text, article_id) VALUES (:comment_id, :text, :article_id)").bind("comment_id",comment.getId()).bind("text", comment.getText()).bind("article_id", comment.getArticleId().getValue())
                    .execute();
            /*List<Comment> commentList=article.getComments();
            commentList.add(comment);
            articleRepository.update(article.withComments(commentList));*/
        }
        /*
        if (commentMap.containsKey(comment.getId())){
            throw new CommentIdDuplicatedException("Comment with the given id already exists: "+comment.getId());
        }
        try{
            Article article=articleRepository.findById(comment.getArticleId().getValue());
            commentMap.put(comment.getId(),comment);
            List<Comment> commentList=article.getComments();
            commentList.add(comment);
            articleRepository.update(article.withComments(commentList));
        }
        catch (ArticleNotFoundException e){
            throw e;
        }*/
    }

    @Override
    public synchronized void update(Comment newComment) {
        try{
            findById(newComment.getId());
        }
        catch (CommentNotFoundException e){
            throw e;
        }
        try(Handle handle = jdbi.open()){
            handle.createUpdate("UPDATE comment SET text=:text WHERE comment_id=:comment_id").bind("comment_id", newComment.getId()).bind("text", newComment.getText())
                    .execute();
            Article article = articleRepository.findById(newComment.getArticleId().getValue());
            List<Comment> comments= new ArrayList<>(article.getComments());
            for (int index=0;index<comments.size();index++){
                if (comments.get(index).getId()==newComment.getId()){
                    comments.set(index, newComment);
                }
            }
            articleRepository.update(article.withComments(comments));
        }
        /*if (!commentMap.containsKey(newComment.getId())){
            throw new CommentNotFoundException("Cannot find comment by id=" + newComment.getId());
        }
        commentMap.put(newComment.getId(),newComment);
        Article article=articleRepository.findById(newComment.getArticleId().getValue());
        List<Comment> comments=article.getComments();
        for (int index=0;index<comments.size();index++){
            if (comments.get(index).getId()==newComment.getId()){
                comments.set(index, newComment);
            }
        }*/
    }

    @Override
    public void delete(long commentId) {
        Comment comment;
        try{
            comment = findById(commentId);
        }
        catch (CommentNotFoundException e){
            throw new CommentNotFoundException("Cannot find comment by id=" + commentId);
        }

        try(Handle handle = jdbi.open()){
            Article article = articleRepository.findById(comment.getArticleId().getValue());
            List<Comment> comments = article.getComments();
            for (int index=0;index<comments.size();index++){
                if (comments.get(index).getId()==commentId){
                    comments.remove(index);
                    break;
                }
            }
            articleRepository.update(article.withComments(comments));
            handle.createUpdate("DELETE FROM comment WHERE comment_id=:comment_id").bind("comment_id", commentId)
                    .execute();
        }

        /*if (!commentMap.containsKey(commentId)){
            throw new CommentNotFoundException("Cannot find comment by id=" + commentId);
        }
        Comment comment=commentMap.get(commentId);
        Article article=articleRepository.findById(comment.getArticleId().getValue());
        List<Comment> comments=article.getComments();
        for (int index=0;index<comments.size();index++){
            if (comments.get(index).getId()==commentId){
                comments.remove(index);
                break;
            }
        }
        articleRepository.update(article.withComments(comments));
        commentMap.remove(commentId);*/
    }
}
