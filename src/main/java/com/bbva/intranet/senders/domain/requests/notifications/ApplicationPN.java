package com.bbva.intranet.senders.domain.requests.notifications;

public class ApplicationPN {
    private Long id;

    public ApplicationPN() {
    }

    public ApplicationPN(Long id) {

        this.id = id;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
