package com.trackme.trackmeapplication.sharedData;

import java.io.Serializable;

/**
 * Position data wrapper object for the mapper. Copy of the position data in DB
 *
 * @author Mattia Tibaldi
 */
public class PositionDataWrapper implements Serializable {

    //attributes
    private String timestamp;
    private String latitude;
    private String longitude;

    /**
     * Constructor
     */
    public PositionDataWrapper(){ }

    //setter methods
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    //getter methods
    public String getTimestamp() {
        return timestamp;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
