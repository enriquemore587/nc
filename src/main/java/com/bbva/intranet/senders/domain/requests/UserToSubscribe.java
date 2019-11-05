package com.bbva.intranet.senders.domain.requests;

public class UserToSubscribe {

    private String user;
    private Long applicationId;

    public UserToSubscribe() {
    }

    public UserToSubscribe(String user, Long applicationId) {

        this.user = user;
        this.applicationId = applicationId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    @Override
    public String toString() {
        return "UserToSubscribe{" +
                "user='" + user + '\'' +
                ", applicationId=" + applicationId +
                '}';
    }
}