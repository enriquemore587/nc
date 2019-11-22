package com.bbva.intranet.senders.domain.requests.desregister;

public class Desregister {
    private String userId;
    private ApplicationDesregister application;
    private DeviceDesregister device;

    public Desregister() {
    }

    public Desregister(String userId, ApplicationDesregister application, DeviceDesregister device) {

        this.userId = userId;
        this.application = application;
        this.device = device;
    }

    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ApplicationDesregister getApplication() {
        return application;
    }

    public void setApplication(ApplicationDesregister application) {
        this.application = application;
    }

    public DeviceDesregister getDevice() {
        return device;
    }

    public void setDevice(DeviceDesregister device) {
        this.device = device;
    }
}
