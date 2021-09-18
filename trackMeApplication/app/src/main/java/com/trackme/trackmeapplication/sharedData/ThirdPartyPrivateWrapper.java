package com.trackme.trackmeapplication.sharedData;

import java.io.Serializable;

/**
 * Third party private wrapper for sending the registration to he server
 *
 * @author Mattia Tibaldi
 */
public class ThirdPartyPrivateWrapper implements Serializable {

    //attributes
    private ThirdPartyCustomer thirdPartyCustomer;
    private PrivateThirdPartyDetail privateThirdPartyDetail;

    /**
     * Constructor
     */
    public ThirdPartyPrivateWrapper(){}

    //setter methods
    public void setPrivateThirdPartyDetail(PrivateThirdPartyDetail privateThirdPartyDetail) {
        this.privateThirdPartyDetail = privateThirdPartyDetail;
    }

    public void setThirdPartyCustomer(ThirdPartyCustomer thirdPartyCustomer) {
        this.thirdPartyCustomer = thirdPartyCustomer;
    }

    //getter methods
    public PrivateThirdPartyDetail getPrivateThirdPartyDetail() {
        return privateThirdPartyDetail;
    }

    public ThirdPartyCustomer getThirdPartyCustomer() {
        return thirdPartyCustomer;
    }
}
