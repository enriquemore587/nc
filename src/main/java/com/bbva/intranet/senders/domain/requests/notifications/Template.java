package com.bbva.intranet.senders.domain.requests.notifications;

import java.util.Map;

public class Template {

    private Long id;
    private Map<String, Object> properties;

    public Template() {
    }

    public Template(Long id, Map<String, Object> properties) {

        this.id = id;
        this.properties = properties;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
