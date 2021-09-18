package com.trackme.trackmeapplication.localdb.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.sql.Timestamp;

@Entity(tableName = "emergency-calls")
public class EmergencyCall {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String phoneNumber = "";

    @NonNull
    private Timestamp timestamp = new Timestamp(0);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @NonNull
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
