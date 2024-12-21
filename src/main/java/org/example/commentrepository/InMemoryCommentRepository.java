package org.example.commentrepository;

import org.example.article.Article;
import org.example.articlerepository.InMemoryArticleRepository;
import org.example.comment.Comment;
import org.example.comment.CommentId;
import org.example.exceptions.ArticleNotFoundException;
import org.example.exceptions.CommentIdDuplicatedException;
import org.example.exceptions.CommentNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryCommentRepository implements CommentRepository {
    private final AtomicLong nextId = new AtomicLong(0);
    private final Map<Long, Comment> commentMap = new ConcurrentHashMap<>();
    private final InMemoryArticleRepository articleRepository;

    public InMemoryCommentRepository(InMemoryArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
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
        Comment comment=commentMap.get(commentId);

        if (comment==null){
            throw new CommentNotFoundException("Cannot find comment by id=" + commentId);
        }
        return comment;
    }

    @Override
    public synchronized void create(Comment comment) {
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
        }
    }

    @Override
    public synchronized void update(Comment newComment) {
        if (!commentMap.containsKey(newComment.getId())){
            throw new CommentNotFoundException("Cannot find comment by id=" + newComment.getId());
        }
        commentMap.put(newComment.getId(),newComment);
        Article article=articleRepository.findById(newComment.getArticleId().getValue());
        List<Comment> comments=article.getComments();
        for (int index=0;index<comments.size();index++){
            if (comments.get(index).getId()==newComment.getId()){
                comments.set(index, newComment);
            }
        }
    }

    @Override
    public void delete(long commentId) {
        if (!commentMap.containsKey(commentId)){
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
        commentMap.remove(commentId);
    }
}
