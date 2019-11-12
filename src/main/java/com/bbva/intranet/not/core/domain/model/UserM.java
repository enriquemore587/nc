package com.bbva.intranet.not.core.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nc_users")
public class UserM {

    @Id
    @GeneratedValue
    private Long id;
    private String email;

    public UserM() {
    }

    public UserM(String email) {

        this.email = email;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
