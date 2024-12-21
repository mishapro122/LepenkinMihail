package org.example.topicrepository;

import org.example.article.Article;
import org.example.article.ArticleId;
import org.example.articlerepository.InMemoryArticleRepository;
import org.example.exceptions.ArticleNotFoundException;
import org.example.exceptions.TopicIdDuplicatedException;
import org.example.exceptions.TopicNotFoundException;
import org.example.topic.Topic;
import org.example.topic.TopicId;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultIterable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTopicRepository implements TopicRepository{
    private final AtomicLong nextId = new AtomicLong(0);
    private final Map<Long, Topic> topicMap = new ConcurrentHashMap<>();
    private final InMemoryArticleRepository articleRepository;
    private final Jdbi jdbi;

    public InMemoryTopicRepository(InMemoryArticleRepository articleRepository, Jdbi jdbi) {
        this.articleRepository = articleRepository;
        this.jdbi = jdbi;
    }


    @Override
    public TopicId generateId() {
        return new TopicId(nextId.incrementAndGet());
    }

    @Override
    public Topic findById(long topicId) {
        Topic topic;
        try(Handle handle = jdbi.open()){
            ResultIterable<Topic> result = handle.createQuery("SELECT topic_id,text,article_id FROM topic WHERE topic_id =:topic_id").bind("topic_id", topicId).map((rs, ctx)-> new Topic(new ArticleId(rs.getLong("article_id")), new TopicId(rs.getLong("topic_id")), rs.getString("text")));
            try{
                topic = result.first();
            }
            catch (Exception e) {
                throw new TopicNotFoundException("cannot find topic by id=" + topicId);
            }
        }
        return topic;
        /*Topic topic = topicMap.get(topicId);
        if (topic == null){
            throw new TopicNotFoundException("cannot find topic by id=" + topicId);
        }
        return topic;*/
    }

    @Override
    public Set<Topic> updateTags(Set<String> newTags, Set<Topic> oldTags, ArticleId articleId){
        Set<String> textOfOldTags = new HashSet<>();
        Set<Topic> newTopics = new HashSet<>(oldTags);
        for (Topic topic : oldTags){
            //textOfOldTags.add(topic.getText());
            if (!newTags.contains(topic.getText())){
                delete(topic.getTopicId());
                newTopics.remove(topic);
            }
            else {
                textOfOldTags.add(topic.getText());
            }
        }
        for (String text : newTags){
            if (!textOfOldTags.contains(text)){
                TopicId topicId = generateId();
                Topic newTopic = new Topic(articleId,topicId,text);
                newTopics.add(newTopic);
                create(newTopic);
            }
        }
        return newTopics;
    }


    @Override
    public synchronized void create(Topic topic) {
        try(Handle handle = jdbi.open()){
            Set<String> set = handle.createQuery("SELECT text, topic_id FROM topic WHERE topic_id=:topic_id").bind("topic_id",topic.getTopicId())
                    .map((rs, ctx) -> rs.getString("text")).set();
            if (!set.isEmpty()){
                throw new TopicIdDuplicatedException("Topic with the given id already exists: "+ topic.getTopicId());
            }
            try{
                Article article = articleRepository.findById(topic.getArticleId().getValue());
                handle.createUpdate("INSERT INTO topic (topic_id, text, article_id) VALUES (:topic_id, :text, :article_id)")
                        .bind("topic_id",topic.getTopicId()).bind("text", topic.getText()).bind("article_id", topic.getArticleId().getValue())
                        .execute();
            }
            catch (ArticleNotFoundException e){
                throw e;
            }
        }
        /*
        if (topicMap.containsKey(topic.getTopicId())){
            throw new TopicIdDuplicatedException("Topic with the given id already exists: "+ topic.getTopicId());
        }
        try{
            Article article = articleRepository.findById(topic.getArticleId().getValue());
            topicMap.put(topic.getTopicId(),topic);
        }
        catch (ArticleNotFoundException e){
            throw e;
        }*/
    }

    @Override
    public void delete(long topicId) {
        try {
            findById(topicId);
        }
        catch (TopicNotFoundException e){
            throw new TopicNotFoundException("cannot find topic by id="+topicId);
        }
        try (Handle handle = jdbi.open()){
            handle.createUpdate("DELETE FROM topic WHERE topic_id=:topic_id").bind("topic_id",topicId)
                    .execute();
        }
        /*if (!topicMap.containsKey(topicId)){
            throw new TopicNotFoundException("cannot find topic by id="+topicId);
        }
        topicMap.remove(topicId);*/
    }
}
