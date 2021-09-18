package com.poianitibaldizhou.trackme.sharedataservice.util;

import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QUnionDataPath;
import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QUser;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;

import java.sql.Timestamp;

/**
 * Type of object that can be requested from a group request
 */
public enum  RequestType {
    ALL(Wildcard.all),
    USER_SSN(Expressions.stringPath(QUnionDataPath.ALIAS_USER_SSN)),
    BIRTH_YEAR(QUser.user.birthDate.year()),
    BIRTH_CITY(QUser.user.birthCity),
    HEART_BEAT(Expressions.numberPath(Integer.class, QUnionDataPath.ALIAS_HEARTBEAT)),
    PRESSURE_MIN(Expressions.numberPath(Integer.class, QUnionDataPath.ALIAS_PRESSURE_MIN)),
    PRESSURE_MAX(Expressions.numberPath(Integer.class, QUnionDataPath.ALIAS_PRESSURE_MAX)),
    BLOOD_OXYGEN_LEVEL(Expressions.numberPath(Integer.class, QUnionDataPath.ALIAS_BLOOD_OXYGEN_LEVEL));

    private Expression<?> fieldPath;

    /**
     * Constructor.
     * Creates a request type which is defined by a boolean isNumber and an expression fieldPath
     *
     * @param fieldPath the field path of the specific request type
     */
    RequestType(Expression<?> fieldPath){
        this.fieldPath = fieldPath;
    }

    /**
     * @return the expression of the field path (useful for dynamic query)
     */
    public Expression getFieldPath() {
        return fieldPath;
    }
}
