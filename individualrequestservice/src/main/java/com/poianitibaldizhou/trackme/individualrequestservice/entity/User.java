package com.poianitibaldizhou.trackme.individualrequestservice.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Information useful to this service regarding the user is present here.
 * The only thing that matters is the ssn (which is the primary key for the table of
 * the users).
 * This, for example, allows to verify that requests are performed on users
 * that are registered.
 */
@Data
@Entity
public class User {
    @Id
    @Column(length = 16)
    private String ssn;

    public User() {

    }

    public User(String ssn) {
        this.ssn = ssn;
    }
}
