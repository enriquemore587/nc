package com.bbva.intranet.senders.domain.responses;

public class GNotifier {
    private  Object data;

    public GNotifier(Object data) {
        this.data = data;
    }

    public GNotifier() {

    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
