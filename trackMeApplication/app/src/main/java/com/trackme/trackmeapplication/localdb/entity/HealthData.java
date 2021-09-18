package com.trackme.trackmeapplication.localdb.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.sql.Timestamp;
import java.util.Comparator;

@Entity(tableName = "health-data")
public class HealthData {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private Timestamp timestamp = new Timestamp(0);

    @NonNull
    private Integer heartbeat = 0;

    @NonNull
    private Integer pressureMin = 0;

    @NonNull
    private Integer pressureMax = 0;

    @NonNull
    private Integer bloodOxygenLevel = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    public Integer getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(@NonNull Integer heartbeat) {
        this.heartbeat = heartbeat;
    }

    @NonNull
    public Integer getPressureMin() {
        return pressureMin;
    }

    public void setPressureMin(@NonNull Integer pressureMin) {
        this.pressureMin = pressureMin;
    }

    @NonNull
    public Integer getPressureMax() {
        return pressureMax;
    }

    public void setPressureMax(@NonNull Integer pressureMax) {
        this.pressureMax = pressureMax;
    }

    @NonNull
    public Integer getBloodOxygenLevel() {
        return bloodOxygenLevel;
    }

    public void setBloodOxygenLevel(@NonNull Integer bloodOxygenLevel) {
        this.bloodOxygenLevel = bloodOxygenLevel;
    }

    public boolean isValidData() {
        return allFieldNotNull() && isBloodOxygenLevelPercentage() && isPressureMaxGreaterThanPressureMin();
    }

    private boolean allFieldNotNull() {
        return timestamp != null && heartbeat != null && pressureMin != null && pressureMax != null
                && bloodOxygenLevel != null;
    }

    private boolean isBloodOxygenLevelPercentage() {
        return bloodOxygenLevel <= 100 && bloodOxygenLevel > 0;
    }

    private boolean isPressureMaxGreaterThanPressureMin() {
        return pressureMax >= pressureMin;
    }

    @Override
    public String toString() {
        return "HealthData{" +
                "timestamp=" + timestamp +
                ", heartbeat=" + heartbeat +
                ", pressureMin=" + pressureMin +
                ", pressureMax=" + pressureMax +
                ", bloodOxygenLevel=" + bloodOxygenLevel +
                '}';
    }

    public static class CustomComparator implements Comparator<HealthData> {
        @Override
        public int compare(HealthData o1, HealthData o2) {
            return o1.getTimestamp().compareTo(o2.getTimestamp());
        }
    }
}
