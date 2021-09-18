package com.trackme.trackmeapplication.localdb.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.trackme.trackmeapplication.localdb.entity.HealthData;

import java.util.List;

@Dao
public interface HealthDataDao {

    @Transaction
    @Query("SELECT * FROM `health-data`")
    List<HealthData> getAll();

    @Transaction
    @Insert
    void insert(HealthData healthData);

    @Transaction
    @Query("DELETE FROM `health-data`")
    void deleteAll();

    @Transaction
    @Query("DELETE FROM `health-data` WHERE id = :elemID")
    void deleteById(int elemID);

    @Transaction
    @Query("SELECT * FROM `health-data` ORDER BY timestamp DESC LIMIT 1")
    HealthData getLast();
}
