package com.bbva.intranet.senders.domain.requests.notifications;

public class Apns extends FCMBasicInfo {

    private Payload payload; // (object, optional): APNs payload as a JSON object, including both aps dictionary and custom payload. See "https://developer.apple.com/library/archive/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/PayloadKeyReference.html#//apple_ref/doc/uid/TP40008194-CH17-SW1". If present, it overrides google.firebase.fcm.v1.Notification.title and google.firebase.fcm.v1.Notification.body.

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Apns{" +
                "payload=" + payload +
                '}';
    }
}
