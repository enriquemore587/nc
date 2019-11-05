package com.bbva.intranet.senders.domain.requests.register;

public class ApplicationToUserToRegister {

    private Long id;
    private String name;
    private String version;
    private String language;

    public ApplicationToUserToRegister() {
    }

    public ApplicationToUserToRegister(Long id, String name, String version, String language) {

        this.id = id;
        this.name = name;
        this.version = version;
        this.language = language;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
