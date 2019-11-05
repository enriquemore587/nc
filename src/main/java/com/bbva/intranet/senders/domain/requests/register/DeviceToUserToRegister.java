package com.bbva.intranet.senders.domain.requests.register;

public class DeviceToUserToRegister {
    private String id;
    private String brand;
    private String model;
    private String os;
    private String osVersion;
    private String language;

    public DeviceToUserToRegister() {
    }

    public DeviceToUserToRegister(String id, String brand, String model, String os, String osVersion, String language) {

        this.id = id;
        this.brand = brand;
        this.model = model;
        this.os = os;
        this.osVersion = osVersion;
        this.language = language;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
