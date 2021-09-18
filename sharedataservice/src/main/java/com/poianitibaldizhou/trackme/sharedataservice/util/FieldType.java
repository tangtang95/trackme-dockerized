package com.poianitibaldizhou.trackme.sharedataservice.util;


import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QHealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QPositionData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QUser;
import com.querydsl.core.types.Expression;

import java.util.Arrays;
import java.util.List;

/**
 * Type of field that can be used on a filter statement
 */
public enum FieldType {
    POSITION_TIMESTAMP(QPositionData.positionData.timestamp), HEALTH_TIMESTAMP(QHealthData.healthData.timestamp),
    LATITUDE(QPositionData.positionData.latitude), LONGITUDE(QPositionData.positionData.longitude),
    HEART_BEAT(QHealthData.healthData.heartBeat), PRESSURE_MIN(QHealthData.healthData.pressureMin),
    PRESSURE_MAX(QHealthData.healthData.pressureMax), BLOOD_OXYGEN_LEVEL(QHealthData.healthData.bloodOxygenLevel),
    BIRTH_CITY(QUser.user.birthCity), BIRTH_NATION(QUser.user.birthNation), BIRTH_YEAR(QUser.user.birthDate.year());

    private Expression<?> expression;

    /**
     * Constructor.
     * Create a FieldType of a specific expression to the db table
     *
     * @param expression the expression of the field
     */
    FieldType(Expression<?> expression){
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public boolean contains(List<FieldType> fieldTypes){
        return fieldTypes.stream().filter(this::equals).count() > 0;
    }

    public static List<FieldType> getPositionDataFields(){
        return Arrays.asList(POSITION_TIMESTAMP, LATITUDE, LONGITUDE);
    }

    public static List<FieldType> getHealthDataFields(){
        return Arrays.asList(HEALTH_TIMESTAMP, HEART_BEAT, PRESSURE_MAX, PRESSURE_MIN, BLOOD_OXYGEN_LEVEL);
    }

    public static List<FieldType> getUserFields(){
        return Arrays.asList(BIRTH_CITY, BIRTH_NATION, BIRTH_YEAR);
    }
}
