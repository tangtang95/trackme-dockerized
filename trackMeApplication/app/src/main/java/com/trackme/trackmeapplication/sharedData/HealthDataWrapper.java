package com.trackme.trackmeapplication.sharedData;

import java.io.Serializable;

/**
 * Heath data object for the mapper. Copy of the entity in the DB
 *
 * @author Mattia Tibaldi
 */
public class HealthDataWrapper implements Serializable {

    //attributes
    private String timestamp;
    private String heartBeat;
    private String pressureMin;
    private String pressureMax;
    private String bloodOxygenLevel;

    /**
     * Constructor
     */
    public HealthDataWrapper(){}

    //setter methods
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setHeartBeat(String heartBeat) {
        this.heartBeat = heartBeat;
    }

    public void setPressureMin(String pressureMin) {
        this.pressureMin = pressureMin;
    }

    public void setPressureMax(String pressureMax) {
        this.pressureMax = pressureMax;
    }

    public void setBloodOxygenLevel(String bloodOxygenLevel) {
        this.bloodOxygenLevel = bloodOxygenLevel;
    }

    //getter methods
    public String getTimestamp() {
        return timestamp;
    }

    public String getHeartBeat() {
        return heartBeat;
    }

    public String getPressureMin() {
        return pressureMin;
    }

    public String getPressureMax() {
        return pressureMax;
    }

    public String getBloodOxygenLevel() {
        return bloodOxygenLevel;
    }
}
