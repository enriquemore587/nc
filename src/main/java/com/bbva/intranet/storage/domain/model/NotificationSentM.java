package com.bbva.intranet.storage.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nc_notifications_sent")
public class NotificationSentM {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "notification_id")
    private Long notificationId;
    private boolean read;

    public NotificationSentM() {
    }

    public NotificationSentM(Long userId, Long notificationId, boolean read) {

        this.userId = userId;
        this.notificationId = notificationId;
        this.read = read;
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

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
