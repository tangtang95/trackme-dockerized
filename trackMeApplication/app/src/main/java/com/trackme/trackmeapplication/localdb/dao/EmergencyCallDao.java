package com.trackme.trackmeapplication.localdb.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.trackme.trackmeapplication.localdb.entity.EmergencyCall;

import java.util.List;

@Dao
public interface EmergencyCallDao {

    @Transaction
    @Query("SELECT * FROM `emergency-calls`")
    List<EmergencyCall> getAll();

    @Transaction
    @Query("SELECT COUNT(*) FROM `emergency-calls` WHERE (julianday('now') - julianday(timestamp)) * 24 * 60 BETWEEN 0 AND 60")
    long getNumberOfRecentCalls();

    @Transaction
    @Query("SELECT (julianday('now') - julianday(timestamp)) * 24 * 60 FROM `emergency-calls`")
    List<Double> getDifference();

    @Transaction
    @Insert
    void insert(EmergencyCall emergencyCall);

    @Transaction
    @Query("DELETE FROM `emergency-calls` WHERE (julianday('now') - julianday(timestamp)) * 24 * 60 BETWEEN 0 AND 60")
    void deleteAllRecentCalls();

}
