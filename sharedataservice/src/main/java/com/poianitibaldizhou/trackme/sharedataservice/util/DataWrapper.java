package com.poianitibaldizhou.trackme.sharedataservice.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper of data to be received from the user
 */
@Data
@JsonIgnoreProperties
public class DataWrapper {

    private List<PositionData> positionDataList;
    private List<HealthData> healthDataList;

    /**
     * Empty constructor: initialize lists
     */
    public DataWrapper(){
        positionDataList = new ArrayList<>();
        healthDataList = new ArrayList<>();
    }

    /**
     * Add the position data to the list of position data
     * @param positionData the position data to be added
     * @return true if the insertion is successful otherwise false
     */
    public boolean addPositionData(PositionData positionData){
        return positionDataList.add(positionData);
    }

    /**
     * Add the health data to the list of position data
     * @param healthData the health data to be added
     * @return true if the insertion is successful otherwise false
     */
    public boolean addHealthData(HealthData healthData){
        return healthDataList.add(healthData);
    }

}
