package org.example.topicrepository;

import org.example.article.ArticleId;
import org.example.topic.Topic;
import org.example.topic.TopicId;

import java.util.Set;

public interface TopicRepository {
    TopicId generateId();

    Topic findById(long topicId);

    void create(Topic topic);

    void delete(long topicId);

    Set<Topic> updateTags(Set<String> newTags, Set<Topic> oldTags, ArticleId articleId);
}
