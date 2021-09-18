package com.trackme.trackmeapplication.sharedData;

import java.io.Serializable;

/**
 * Third party customer info with username/mail and password
 *
 * @author Mattia Tibaldi
 */
public class ThirdPartyCustomer implements Serializable {

    //attributes
    private String email;
    private String username;
    private String password;

    /**
     * Constructor for the mapper
     */
    public ThirdPartyCustomer(){}

    /**
     * Constructor
     *
     * @param email third party e-mail
     * @param password password
     */
    public ThirdPartyCustomer(String email, String password) {
        this.email = email;
        this.password = password;
    }

    //getter methods
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
