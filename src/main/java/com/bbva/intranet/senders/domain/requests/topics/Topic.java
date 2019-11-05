package com.bbva.intranet.senders.domain.requests.topics;

public class Topic {
    private String name;
    private String description;
    private String sender_id;

    public Topic() {
    }

    public Topic(String name, String description, String sender_id) {
        this.name = name;
        this.description = description;
        this.sender_id = sender_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }
}
