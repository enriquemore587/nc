package com.bbva.intranet.senders.domain.requests.notifications;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class FCMBasicInfo {

    private Map<String, String> headers; // (object, optional): HTTP headers defined in webpush protocol. Refer to "https://tools.ietf.org/html/rfc8030#section-5" for supported headers.,
    @SerializedName("fcm_options")
    private Map<String, String> fcmOptions; // (object, optional): Options for features provided by the FCM SDK for iOS.,

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getFcmOptions() {
        return fcmOptions;
    }

    public void setFcmOptions(Map<String, String> fcmOptions) {
        this.fcmOptions = fcmOptions;
    }
}
