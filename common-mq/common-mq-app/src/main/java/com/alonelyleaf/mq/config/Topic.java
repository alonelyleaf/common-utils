package com.alonelyleaf.mq.config;

/**
 * @author bijl
 * @date 2019/5/27
 */
public enum  Topic {

    MESSAGE("message", "message");

    private String topic;

    private String description;

    Topic(String topic, String description){
        this.topic = topic;
        this.description = description;
    }

    public String getTopic() {
        return topic;
    }

    public Topic setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Topic setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getTopic(Topic topic){

        return topic.getTopic();
    }
}
