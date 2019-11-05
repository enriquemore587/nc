package com.bbva.intranet.ns.utilities;

public enum NSChannel {

    GNOTIFIER("gnotifier"),
    FCM("fcm");
    public String value;

    NSChannel(String value) {
        this.value = value;
    }

    public boolean is(String channel) {
        return channel.equalsIgnoreCase(value);
    }
}
