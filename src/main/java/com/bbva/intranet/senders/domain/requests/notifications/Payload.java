package com.bbva.intranet.senders.domain.requests.notifications;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Payload {

    private Map<String, String> alert;
    private Integer badge;
    private String sound;
    @SerializedName("content-available")
    private Integer contentAvailable;
    private String category;
    @SerializedName("thread-id")
    private String threadId;

    @Override
    public String toString() {
        return "Payload{" +
                "alert=" + alert +
                ", badge=" + badge +
                ", sound='" + sound + '\'' +
                ", contentAvailable=" + contentAvailable +
                ", category='" + category + '\'' +
                ", threadId='" + threadId + '\'' +
                '}';
    }
}