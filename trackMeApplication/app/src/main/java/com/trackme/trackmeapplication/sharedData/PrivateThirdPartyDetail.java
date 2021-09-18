package com.trackme.trackmeapplication.sharedData;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Private third party detail with all attribute of a private user.
 *
 * @author Mattia Tibaldi
 * @see ThirdPartyInterface
 * @see ThirdPartyCustomer
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrivateThirdPartyDetail implements ThirdPartyInterface {

    //attributes
    private ThirdPartyCustomer thirdPartyCustomer;
    private String ssn;
    private String name;
    private String surname;
    private Date birthDate;
    private String birthCity;

    /**
     * Constructor
     */
    public PrivateThirdPartyDetail(){}

    /**
     * Constructor
     *
     * @param ssn third party ssn
     * @param thirdPartyCustomer email and password
     * @param firstName user name
     * @param lastName user surname
     * @param birthDate birth date
     * @param birthCity birth city
     */
    public PrivateThirdPartyDetail(String ssn, ThirdPartyCustomer thirdPartyCustomer, String firstName, String lastName, Date birthDate, String birthCity) {
        this.ssn = ssn;
        this.thirdPartyCustomer = thirdPartyCustomer;
        this.name = firstName;
        this.surname = lastName;
        this.birthDate = birthDate;
        this.birthCity = birthCity;

    }

    //getter method
    @Override
    public String extractName() {
        return name + " " + surname;
    }

    @Override
    public String extractEmail() {
        return thirdPartyCustomer.getEmail();
    }

    @Override
    public String extractPassword() {return thirdPartyCustomer.getPassword();}

    public String getSsn() {
        return ssn;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public ThirdPartyCustomer getThirdPartyCustomer() {
        return thirdPartyCustomer;
    }

    public String getSurname() {
        return surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getName() {
        return name;
    }
}
