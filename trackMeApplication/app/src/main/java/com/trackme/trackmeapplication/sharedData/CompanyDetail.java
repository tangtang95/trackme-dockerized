package com.trackme.trackmeapplication.sharedData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Company detail object, with all the company attributes
 *
 * @author Mattia Tibaldi
 * @see ThirdPartyCustomer
 * @see ThirdPartyInterface
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyDetail implements ThirdPartyInterface{

    //attributes
    private ThirdPartyCustomer thirdPartyCustomer;
    private String companyName;
    private String address;
    private String dunsNumber;

    /**
     * Constructor for object mapper
     */
    public CompanyDetail(){}

    /**
     * Constructor
     *
     * @param companyName company name
     * @param thirdPartyCustomer mail and password
     * @param address address of company
     * @param dunsNumber duns number of the company
     */
    public CompanyDetail(String companyName, ThirdPartyCustomer thirdPartyCustomer, String address, String dunsNumber) {
        this.companyName = companyName;
        this.thirdPartyCustomer = thirdPartyCustomer;
        this.address = address;
        this.dunsNumber = dunsNumber;
    }

    //getter method
    public String getAddress() {
        return address;
    }

    public String getDunsNumber() {
        return dunsNumber;
    }

    @Override
    public String extractName() {
        return companyName;
    }

    @Override
    public String extractEmail() {
        return thirdPartyCustomer.getEmail();
    }

    @Override
    public String extractPassword() {
        return thirdPartyCustomer.getPassword();
    }


    public String getCompanyName() {
        return companyName;
    }

    public ThirdPartyCustomer getThirdPartyCustomer() {
        return thirdPartyCustomer;
    }
}
