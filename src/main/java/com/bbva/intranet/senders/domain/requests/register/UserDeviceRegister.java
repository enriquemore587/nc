package com.bbva.intranet.senders.domain.requests.register;

public class UserDeviceRegister {
    private String userId;
    private ApplicationToUserToRegister application;
    private DeviceToUserToRegister device;
    private String token;

    public UserDeviceRegister() {
    }

    public UserDeviceRegister(String userId, ApplicationToUserToRegister application, DeviceToUserToRegister device, String token) {

        this.userId = userId;
        this.application = application;
        this.device = device;
        this.token = token;
    }

    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ApplicationToUserToRegister getApplication() {
        return application;
    }

    public void setApplication(ApplicationToUserToRegister application) {
        this.application = application;
    }

    public DeviceToUserToRegister getDevice() {
        return device;
    }

    public void setDevice(DeviceToUserToRegister device) {
        this.device = device;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
