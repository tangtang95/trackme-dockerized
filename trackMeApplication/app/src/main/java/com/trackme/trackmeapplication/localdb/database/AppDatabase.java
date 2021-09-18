package com.trackme.trackmeapplication.localdb.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.trackme.trackmeapplication.localdb.Converters;
import com.trackme.trackmeapplication.localdb.dao.EmergencyCallDao;
import com.trackme.trackmeapplication.localdb.dao.HealthDataDao;
import com.trackme.trackmeapplication.localdb.dao.PositionDataDao;
import com.trackme.trackmeapplication.localdb.entity.EmergencyCall;
import com.trackme.trackmeapplication.localdb.entity.HealthData;
import com.trackme.trackmeapplication.localdb.entity.PositionData;

@Database(entities = {HealthData.class, PositionData.class, EmergencyCall.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract PositionDataDao getPositionDataDao();
    public abstract HealthDataDao getHealthDataDao();
    public abstract EmergencyCallDao getEmergencyCallDao();

}
