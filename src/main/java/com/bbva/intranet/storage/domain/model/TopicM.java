package com.bbva.intranet.storage.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nc_topics")
public class TopicM {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;

    public TopicM() {
    }

    public TopicM(String name) {
        this.name = name;
    }

    public TopicM(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
