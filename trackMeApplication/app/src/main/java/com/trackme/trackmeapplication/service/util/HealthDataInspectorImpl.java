package com.trackme.trackmeapplication.service.util;

import com.trackme.trackmeapplication.localdb.entity.HealthData;
import com.trackme.trackmeapplication.service.exception.InvalidHealthDataException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HealthDataInspectorImpl implements HealthDataInspector {

    private final Date mBirthDate;
    private final ThresholdInteger pressureMinThreshold;
    private final ThresholdInteger pressureMaxThreshold;
    private final ThresholdInteger bloodOxygenLevelThreshold;

    public HealthDataInspectorImpl(Date birthDate) {
        mBirthDate = birthDate;
        pressureMinThreshold = new ThresholdInteger(100, 40);
        pressureMaxThreshold = new ThresholdInteger(200, 80);
        bloodOxygenLevelThreshold = new ThresholdInteger(100, 80);
    }

    @Override
    public boolean isGraveCondition(HealthData healthData) throws InvalidHealthDataException {
        if(!healthData.isValidData()) throw new InvalidHealthDataException(healthData);
        ThresholdInteger heartbeatThreshold = getNewHearthBeatThreshold(healthData.getTimestamp());
        if(healthData.getTimestamp().after(mBirthDate)) {
            return !(heartbeatThreshold.contains(healthData.getHeartbeat()) &&
                    pressureMinThreshold.contains(healthData.getPressureMin()) &&
                    pressureMaxThreshold.contains(healthData.getPressureMax()) &&
                    bloodOxygenLevelThreshold.contains(healthData.getBloodOxygenLevel()));
        }
        else {
            return false;
        }
    }

    /**
     * @param dataCreationDate the date and time when the data is generated
     * @return the new threshold integer for the heartbeat
     */
    private ThresholdInteger getNewHearthBeatThreshold(Date dataCreationDate){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dataCreationDate);
        Integer currentYear = calendar.get(Calendar.YEAR);

        calendar.setTime(mBirthDate);
        Integer birthYear =  calendar.get(Calendar.YEAR);
        return new ThresholdInteger(220 - (currentYear - birthYear), 30);
    }
}
