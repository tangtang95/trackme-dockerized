package com.trackme.trackmeapplication.sharedData;

import com.trackme.trackmeapplication.localdb.entity.HealthData;
import com.trackme.trackmeapplication.localdb.entity.PositionData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Cluster data wrapper object to send a cluster data to the server
 *
 * @author Mattia Tibaldi
 */
public class ClusterDataWrapper implements Serializable {

    //attributes
    private List<HealthDataWrapper> healthDataList;
    private List<PositionDataWrapper> positionDataList;

    /**
     * Constructor
     */
    public ClusterDataWrapper() {
        healthDataList = new ArrayList<>();
        positionDataList = new ArrayList<>();
    }

    //getter methods
    public List<HealthDataWrapper> getHealthDataList() {
        return healthDataList;
    }

    public List<PositionDataWrapper> getPositionDataList() {
        return positionDataList;
    }

    public void addHealthData(HealthData hd) {
        HealthDataWrapper healthData = new HealthDataWrapper();
        healthData.setTimestamp(hd.getTimestamp().toString().replace(" ", "T"));
        healthData.setBloodOxygenLevel(hd.getBloodOxygenLevel().toString());
        healthData.setHeartBeat(hd.getHeartbeat().toString());
        healthData.setPressureMax(hd.getPressureMax().toString());
        healthData.setPressureMin(hd.getPressureMin().toString());
        healthDataList.add(healthData);
    }

    public void addPositionData(PositionData pd){
        PositionDataWrapper positionData = new PositionDataWrapper();
        positionData.setLatitude(pd.getLatitude().toString());
        positionData.setLongitude(pd.getLongitude().toString());
        positionData.setTimestamp(pd.getTimestamp().toString().replace(" ", "T"));
        positionDataList.add(positionData);
    }
}
