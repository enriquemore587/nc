package com.bbva.intranet.senders.domain.requests.notifications;

public class Message {

    private Template template;
    private String language;

    public Message() {
    }

    public Message(Template template, String language) {

        this.template = template;
        this.language = language;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "Message{" +
                "template=" + template +
                ", language='" + language + '\'' +
                '}';
    }
}
