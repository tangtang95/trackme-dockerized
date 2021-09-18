package com.poianitibaldizhou.trackme.sharedataservice.entity.domain;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import java.sql.Timestamp;

public class QUnionDataPath {

    public static final String ALIAS_USER_SSN = "userSsn";
    public static final String ALIAS_LATITUDE = "latitude";
    public static final String ALIAS_LONGITUDE = "longitude";
    public static final String ALIAS_TIMESTAMP = "creationTimestamp";
    public static final String ALIAS_HEARTBEAT = "heartbeat";
    public static final String ALIAS_BLOOD_OXYGEN_LEVEL = "bloodOxygenLevel";
    public static final String ALIAS_PRESSURE_MIN = "pressureMin";
    public static final String ALIAS_PRESSURE_MAX = "pressureMax";

    public final Path<Void> alias;
    public final StringPath userSsn;
    public final DateTimePath<Timestamp> timestamp;
    public final NumberPath<Double> latitude;
    public final NumberPath<Double> longitude;
    public final NumberPath<Integer> bloodOxygenLevel;
    public final NumberPath<Integer> heartBeat;
    public final NumberPath<Integer> pressureMax;
    public final NumberPath<Integer> pressureMin;


    public QUnionDataPath(String aliasName){
        this.alias = Expressions.path(Void.class, aliasName);
        this.userSsn = Expressions.stringPath(alias, ALIAS_USER_SSN);
        this.latitude = Expressions.numberPath(Double.class, alias, ALIAS_LATITUDE);
        this.longitude = Expressions.numberPath(Double.class, alias, ALIAS_LONGITUDE);
        this.timestamp = Expressions.dateTimePath(Timestamp.class, alias, ALIAS_TIMESTAMP);
        this.heartBeat = Expressions.numberPath(Integer.class, alias, ALIAS_HEARTBEAT);
        this.bloodOxygenLevel = Expressions.numberPath(Integer.class, alias, ALIAS_BLOOD_OXYGEN_LEVEL);
        this.pressureMin = Expressions.numberPath(Integer.class, alias, ALIAS_PRESSURE_MIN);
        this.pressureMax = Expressions.numberPath(Integer.class, alias, ALIAS_PRESSURE_MAX);
    }

    public Path<?>[] all(){
         return new Path[]{userSsn, timestamp, latitude, longitude, heartBeat, pressureMin,
                 pressureMax, bloodOxygenLevel};
    }

}
