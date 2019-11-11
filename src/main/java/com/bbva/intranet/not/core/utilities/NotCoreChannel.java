package com.bbva.intranet.not.core.utilities;

public enum NotCoreChannel {

    GNOTIFIER("gnotifier"),
    FCM("fcm");
    public String value;

    NotCoreChannel(String value) {
        this.value = value;
    }

    public boolean is(String channel) {
        return channel.equalsIgnoreCase(value);
    }
}
