package com.bbva.intranet.storage.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="nc_devices")
public class DeviceM {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "device_id")
    private String deviceId;
    private String brand;
    private String model;
    private String os;
    @Column(name = "os_version")
    private String osVersion;
    private String language;
    private String token;
    @Column(name = "user_id")
    private Long userId;
    private Date createAt;
    private Date updateAt;

    public DeviceM() {
    }

    public DeviceM(String deviceId, String brand, String model, String os, String osVersion, String language, String token, Long userId, Date createAt, Date updateAt) {
        this.deviceId = deviceId;
        this.brand = brand;
        this.model = model;
        this.os = os;
        this.osVersion = osVersion;
        this.language = language;
        this.token = token;
        this.userId = userId;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
