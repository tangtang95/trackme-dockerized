package com.trackme.trackmeapplication.home.userHome;

import java.io.Serializable;

/**
 * History item object, represent an element in the list of data showed to the user.
 *
 * @author Mattia Tibaldi
 */
public class HistoryItem implements Serializable {

    private String timestamp;
    private String heartBeat;
    private String bloodOxygenLevel;
    private String pressureMin;
    private String pressureMax;

    public HistoryItem(){}

    /**
     * Getter method.
     *
     * @return a string with heartBeat and blood pressure values
     */
    String getCompactInfo() {
        return "Pulse: " + heartBeat + " Pressure: " + pressureMax + "/" + pressureMin + " B.O.L: " + bloodOxygenLevel;
    }

    /*Getter method*/

    public String getTimestamp() {
        return timestamp.substring(0,10);
    }

    public String getBloodOxygenLevel() {
        return bloodOxygenLevel;
    }

    public String getPressureMin() {
        return pressureMin;
    }

    public String getPressureMax() {
        return pressureMax;
    }

    public String getHeartBeat() {
        return heartBeat;
    }

}
