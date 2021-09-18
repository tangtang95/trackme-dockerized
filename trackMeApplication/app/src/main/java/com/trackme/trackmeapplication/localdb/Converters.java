package com.trackme.trackmeapplication.localdb;

import android.arch.persistence.room.TypeConverter;

import java.sql.Timestamp;

public class Converters {
    @TypeConverter
    public static Timestamp fromTimestamp(String stringFormat) {
        return stringFormat == null ? null : Timestamp.valueOf(stringFormat);
    }

    @TypeConverter
    public static String dateToTimestamp(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toString();
    }
}