package com.bbva.intranet.senders.domain.requests.desregister;

public class DeviceDesregister {

    private String id;

    public DeviceDesregister() {
    }

    public DeviceDesregister(String id) {

        this.id = id;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
