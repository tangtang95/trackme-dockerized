package com.trackme.trackmeapplication.localdb.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.trackme.trackmeapplication.localdb.entity.PositionData;

import java.util.List;

@Dao
public interface PositionDataDao {

    @Transaction
    @Query("SELECT * FROM `position-data`")
    List<PositionData> getAll();

    @Transaction
    @Insert
    void insert(PositionData positionData);

    @Transaction
    @Query("DELETE FROM `position-data` WHERE id = :elemID")
    void deleteById(int elemID);

    @Transaction
    @Query("DELETE FROM `position-data`")
    void deleteAll();

}
