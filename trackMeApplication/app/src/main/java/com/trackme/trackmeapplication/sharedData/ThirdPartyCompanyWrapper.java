package com.trackme.trackmeapplication.sharedData;

import java.io.Serializable;

/**
 * Third party company wrapper for the parsing of the response.
 *
 * @author Mattia Tibaldi
 */
public class ThirdPartyCompanyWrapper implements Serializable {

    //attributes
    private ThirdPartyCustomer thirdPartyCustomer;
    private CompanyDetail companyDetail;

    /**
     * Constructor
     */
    public ThirdPartyCompanyWrapper(){}

    //setter method
    public void setCompanyDetail(CompanyDetail companyDetail) {
        this.companyDetail = companyDetail;
    }

    public void setThirdPartyCustomer(ThirdPartyCustomer thirdPartyCustomer) {
        this.thirdPartyCustomer = thirdPartyCustomer;
    }

    //getter method
    public CompanyDetail getCompanyDetail() {
        return companyDetail;
    }

    public ThirdPartyCustomer getThirdPartyCustomer() {
        return thirdPartyCustomer;
    }
}
