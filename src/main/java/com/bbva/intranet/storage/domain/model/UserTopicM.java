package com.bbva.intranet.storage.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nc_user_topic")
public class UserTopicM {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "topic_id")
    private Long topicId;

    public UserTopicM() {
    }

    public UserTopicM(Long userId, Long topicId) {

        this.userId = userId;
        this.topicId = topicId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
}
