package org.example.topic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.article.ArticleId;

import java.util.Objects;

public class Topic {
    private final ArticleId articleId;
    private final TopicId topicId;
    private final String text;
    @JsonCreator
    public Topic(@JsonProperty("articleId") ArticleId articleId, @JsonProperty("id") TopicId topicId, @JsonProperty("tag") String topic) {
        this.articleId = articleId;
        this.topicId = topicId;
        this.text = topic;
    }

    public ArticleId getArticleId() {
        return articleId;
    }

    public String getText() {
        return text;
    }

    public long getTopicId() {
        return topicId.getValue();
    }

    @Override
    public int hashCode(){
        return Objects.hash(articleId,topicId, text);
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Topic topic){
            return topic.articleId.equals(articleId) && topic.topicId.equals(topicId) && topic.text.equals(text);
        }
        return false;
    }

    @Override
    public String toString() {
        return text;
    }
}
