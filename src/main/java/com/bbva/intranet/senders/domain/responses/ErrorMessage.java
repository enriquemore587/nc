package com.bbva.intranet.senders.domain.responses;

import java.util.List;

public class ErrorMessage {

    private String message;
    private String code;
    private String type;
    private List<String> parameters;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
