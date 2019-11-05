package com.bbva.intranet.senders.domain.requests.notifications;

import java.util.Map;

public class WebPush extends FCMBasicInfo {

    private Map<String, String> data; // (object, optional): Arbitrary key/value payload. If present, it will override google.firebase.fcm.v1.Message.data.,
    private Map<String, String> notification; // (object, optional): Web Notification options as a JSON object. Supports Notification instance properties as defined in Web Notification API (https://developer.mozilla.org/en-US/docs/Web/API/Notification). If present, "title" and "body" fields override google.firebase.fcm.v1.Notification.title and google.firebase.fcm.v1.Notification.body.

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public Map<String, String> getNotification() {
        return notification;
    }

    public void setNotification(Map<String, String> notification) {
        this.notification = notification;
    }

    @Override
    public String toString() {
        return "WebPush{" +
                "data=" + data +
                ", notification=" + notification +
                '}';
    }
}
