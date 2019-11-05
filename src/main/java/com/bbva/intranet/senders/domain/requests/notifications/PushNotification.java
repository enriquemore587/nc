package com.bbva.intranet.senders.domain.requests.notifications;

public class PushNotification {

    private ApplicationPN application;
    private String topic;
    private String userId;
    private Message message;
    private Apns apns; // (Apns, optional),
    private WebPush webpush; // (WebPush, optional),
    private Android android; // (Android, optional)

    public PushNotification() {
    }

    public PushNotification(ApplicationPN application, String topic, String userId, Message message, Apns apns, WebPush webpush, Android android) {

        this.application = application;
        this.topic = topic;
        this.userId = userId;
        this.message = message;
        this.apns = apns;
        this.webpush = webpush;
        this.android = android;
    }

    public ApplicationPN getApplication() {

        return application;
    }

    public void setApplication(ApplicationPN application) {
        this.application = application;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Apns getApns() {
        return apns;
    }

    public void setApns(Apns apns) {
        this.apns = apns;
    }

    public WebPush getWebpush() {
        return webpush;
    }

    public void setWebpush(WebPush webpush) {
        this.webpush = webpush;
    }

    public Android getAndroid() {
        return android;
    }

    public void setAndroid(Android android) {
        this.android = android;
    }
}
