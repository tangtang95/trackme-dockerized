package com.trackme.trackmeapplication.localdb.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.sql.Timestamp;
import java.util.Comparator;

@Entity(tableName = "position-data")
public class PositionData {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private Timestamp timestamp = new Timestamp(0);

    @NonNull
    private Double latitude = 0.0;

    @NonNull
    private Double longitude = 0.0;

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
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(@NonNull Double latitude) {
        this.latitude = latitude;
    }

    @NonNull
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(@NonNull Double longitude) {
        this.longitude = longitude;
    }

    public static class CustomComparator implements Comparator<PositionData> {
        @Override
        public int compare(PositionData o1, PositionData o2) {
            return o1.getTimestamp().compareTo(o2.getTimestamp());
        }
    }
}
