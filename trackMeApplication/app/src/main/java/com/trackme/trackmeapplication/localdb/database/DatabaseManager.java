package com.trackme.trackmeapplication.localdb.database;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.trackme.trackmeapplication.R;

public class DatabaseManager {

    private static AppDatabase appDatabase;

    public static AppDatabase getInstance(Context context){
        if(appDatabase == null)
            appDatabase = Room.databaseBuilder(context, AppDatabase.class,
                    context.getString(R.string.persistent_database_name)).build();
        return appDatabase;
    }

}
