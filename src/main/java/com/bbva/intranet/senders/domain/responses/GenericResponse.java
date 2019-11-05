package com.bbva.intranet.senders.domain.responses;

import java.util.List;

public class GenericResponse {

    private Object code;
    private String message;
    private Object data;
    private List<ErrorMessage> messages;
    private String type;
    private List<String> parameters;
    private String error;

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<ErrorMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ErrorMessage> messages) {
        this.messages = messages;
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "GenericResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", messages=" + messages +
                ", type='" + type + '\'' +
                ", parameters=" + parameters +
                ", error='" + error + '\'' +
                '}';
    }
}
