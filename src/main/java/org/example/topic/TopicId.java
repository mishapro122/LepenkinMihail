package org.example.topic;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public class TopicId {
    @JsonValue
    private final long value;

    public TopicId(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o){
        if (this==o){
            return true;
        }
        if (o==null || getClass()!=o.getClass()){
            return false;
        }
        TopicId topicId = (TopicId) o;
        return value==topicId.value;
    }
    @Override
    public int hashCode(){
        return Objects.hash(value);
    }
}
