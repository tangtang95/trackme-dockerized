package com.trackme.bluetoothclient;

import java.sql.Timestamp;

public class HealthData {

    private Timestamp timestamp;
    private int heartbeat;
    private int pressureMin;
    private int pressureMax;
    private int bloodOxygenLevel;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }

    public int getPressureMin() {
        return pressureMin;
    }

    public void setPressureMin(int pressureMin) {
        this.pressureMin = pressureMin;
    }

    public int getPressureMax() {
        return pressureMax;
    }

    public void setPressureMax(int pressureMax) {
        this.pressureMax = pressureMax;
    }

    public int getBloodOxygenLevel() {
        return bloodOxygenLevel;
    }

    public void setBloodOxygenLevel(int bloodOxygenLevel) {
        this.bloodOxygenLevel = bloodOxygenLevel;
    }
}
