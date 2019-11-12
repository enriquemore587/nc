package com.bbva.intranet.not.core.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "nc_notifications")
public class NotificationM {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String body;
    @Column(name = "day_to_notify")
    private Date dayToNotify;

    public NotificationM() {
    }

    public NotificationM(String title, String body, Date dayToNotify) {

        this.title = title;
        this.body = body;
        this.dayToNotify = dayToNotify;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDayToNotify() {
        return dayToNotify;
    }

    public void setDayToNotify(Date dayToNotify) {
        this.dayToNotify = dayToNotify;
    }
}
