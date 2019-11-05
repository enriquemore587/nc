package com.bbva.intranet.nc.utilities;

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
