package com.trackme.trackmeapplication.request.individualRequest;

import java.io.Serializable;

/**
 * Individual request object
 *
 * @author Mattia Tibaldi
 */
public class IndividualRequest implements Serializable {

    //attributes
    private String startDate;
    private String endDate;
    private String motivation;

    /**
     * Constructor
     *
     * @param startDate start date
     * @param endDate end date
     * @param motivation motivation of the request
     */
    IndividualRequest(String startDate, String endDate, String motivation) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.motivation = motivation;
    }

    //getter methods
    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getMotivation() {
        return motivation;
    }
}
