package com.bbva.intranet.senders.domain.requests.desregister;

public class ApplicationDesregister {

    private Long id;

    public ApplicationDesregister() {
    }

    public ApplicationDesregister(Long id) {

        this.id = id;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
