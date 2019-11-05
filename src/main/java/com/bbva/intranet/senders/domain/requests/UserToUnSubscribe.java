package com.bbva.intranet.senders.domain.requests;

public class UserToUnSubscribe {

    private Long applicationId;
    private String userId;

    public UserToUnSubscribe() {
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
