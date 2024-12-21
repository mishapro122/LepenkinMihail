package org.example.commentrepository;

import org.example.article.Article;
import org.example.article.ArticleId;
import org.example.articlerepository.InMemoryArticleRepository;
import org.example.comment.Comment;
import org.example.comment.CommentId;
import org.example.commentrepository.InMemoryCommentRepository;
import org.example.exceptions.ArticleNotFoundException;
import org.example.exceptions.CommentIdDuplicatedException;
import org.example.exceptions.CommentNotFoundException;
import org.example.topic.Topic;
import org.example.topic.TopicId;
import org.example.topicrepository.InMemoryTopicRepository;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
class InMemoryCommentRepositoryTest {
    @Container
    public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:13");
    private static Jdbi jdbi;
    InMemoryArticleRepository articleRepository;
    InMemoryTopicRepository topicRepository;
    InMemoryCommentRepository commentRepository;
    Article testArticle = new Article(new ArticleId(1), "123", Set.of(new Topic(new ArticleId(1), new TopicId(1), "123")), new ArrayList<>(), false);
    Comment testComment = new Comment(new CommentId(1), new ArticleId(1), "123");

    @BeforeAll
    static void beforeAll() {
        String postgresJdbcUrl = POSTGRES.getJdbcUrl();
        Flyway flyway = Flyway.configure()
                .outOfOrder(true)
                .locations("classpath:db/migrations")
                .dataSource(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword())
                .load();
        flyway.migrate();
        jdbi = Jdbi.create(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword());
    }

    @BeforeEach
    void beforeEach() {
        jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM article").execute());
        jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM topic").execute());
        jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM comment").execute());
        articleRepository = new InMemoryArticleRepository(jdbi);
        topicRepository = new InMemoryTopicRepository(articleRepository, jdbi);
        commentRepository = new InMemoryCommentRepository(articleRepository, jdbi);
    }

    @Test
    void shouldFindComment() {
        articleRepository.create(testArticle, topicRepository);
        Comment comment = new Comment(new CommentId(1), new ArticleId(testArticle.getId()), "qwe");
        commentRepository.create(comment);
        assertEquals(commentRepository.findById(1), comment);
    }

    @Test
    void shouldThrowCommentNotFoundExceptionInFind() {
        assertThrows(CommentNotFoundException.class, () -> commentRepository.findById(1));
    }

    @Test
    void shouldCreateComment() {
        articleRepository.create(testArticle, topicRepository);
        Comment comment = new Comment(new CommentId(1), new ArticleId(testArticle.getId()), "qwe");
        commentRepository.create(comment);
        assertEquals(commentRepository.findById(1), comment);
    }

    @Test
    void shouldThrowCommentIdDuplicatedExceptionInCreate() {
        articleRepository.create(testArticle, topicRepository);
        Comment comment = new Comment(new CommentId(1), new ArticleId(testArticle.getId()), "qwe");
        commentRepository.create(comment);
        assertThrows(CommentIdDuplicatedException.class, () -> commentRepository.create(new Comment(new CommentId(1), new ArticleId(1), "qwe")));
    }

    @Test
    void shouldThrowArticleNotFoundExceptionInCreate() {
        assertThrows(ArticleNotFoundException.class, () -> commentRepository.create(new Comment(new CommentId(1), new ArticleId(1), "qwe")));
    }

    @Test
    void shouldUpdateComment() {
        articleRepository.create(testArticle, topicRepository);
        Comment comment = new Comment(new CommentId(1), new ArticleId(testArticle.getId()), "qwe");
        Comment comment1 = comment.withText("q");
        commentRepository.create(comment);
        commentRepository.update(comment1);
        assertEquals(comment1, commentRepository.findById(1));
    }

    @Test
    void shouldThrowCommentNotFoundException() {
        assertThrows(CommentNotFoundException.class, () -> commentRepository.update(testComment));
    }

    @Test
    void shouldDeleteComment() {
        articleRepository.create(testArticle, topicRepository);
        commentRepository.create(testComment);
        commentRepository.delete(testComment.getId());
        assertEquals(articleRepository.findById(testArticle.getId()).getComments().size(), 0);
        assertThrows(CommentNotFoundException.class, () -> commentRepository.findById(testComment.getId()));
    }

    @Test
    void shouldThrowCommentNotFoundExceptionInDelete() {
        assertThrows(CommentNotFoundException.class, () -> commentRepository.delete(1));
    }


}
