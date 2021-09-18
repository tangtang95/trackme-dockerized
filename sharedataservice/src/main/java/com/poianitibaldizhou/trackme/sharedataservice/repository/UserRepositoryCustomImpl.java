package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QHealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QPositionData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QUnionDataPath;
import com.poianitibaldizhou.trackme.sharedataservice.entity.domain.QUser;
import com.poianitibaldizhou.trackme.sharedataservice.util.PredicateBuilder;
import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatorOperator;
import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatorOperatorUtils;
import com.poianitibaldizhou.trackme.sharedataservice.util.FieldType;
import com.poianitibaldizhou.trackme.sharedataservice.util.RequestType;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLTemplates;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Implementation of the custom user repository
 */
@Slf4j
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Double getAggregatedData(AggregatorOperator aggregatorOperator, RequestType requestType, List<FilterStatement> filters) {
        QUser user = QUser.user;
        QHealthData healthData= QHealthData.healthData;
        QPositionData positionData = QPositionData.positionData;
        QUnionDataPath unionDataPath = new QUnionDataPath("unionData");

        PredicateBuilder predicateBuilder = new PredicateBuilder();
        filters.stream().filter(filterStatement -> filterStatement.getColumn().contains(FieldType.getHealthDataFields()))
                .forEach(predicateBuilder::addFilterStatement);
        List<String> ssnFilterFromHealthData = query().select(healthData.userSsn)
                .from(healthData).where(predicateBuilder.build()).fetch();

        filters.stream().filter(filterStatement -> filterStatement.getColumn().contains(FieldType.getPositionDataFields()))
                .forEach(predicateBuilder::addFilterStatement);
        List<String> ssnFilterFromPositionData = query().select(positionData.userSsn)
                .from(positionData).where(predicateBuilder.build()).fetch();



        JPASQLQuery healthQuery = query().from(healthData).select(
                healthData.userSsn.as(unionDataPath.userSsn),
                healthData.timestamp.as(unionDataPath.timestamp),
                Expressions.as(null, unionDataPath.latitude),
                Expressions.as(null, unionDataPath.longitude),
                healthData.heartBeat.as(unionDataPath.heartBeat),
                healthData.pressureMin.as(unionDataPath.pressureMin),
                healthData.pressureMax.as(unionDataPath.pressureMax),
                healthData.bloodOxygenLevel.as(unionDataPath.bloodOxygenLevel))
                .where(healthData.userSsn.in(ssnFilterFromHealthData)
                        .and(healthData.userSsn.in(ssnFilterFromPositionData)));
        JPASQLQuery positionQuery = query().from(positionData).select(
                positionData.userSsn.as(unionDataPath.userSsn),
                positionData.timestamp.as(unionDataPath.timestamp),
                positionData.latitude.as(unionDataPath.latitude),
                positionData.longitude.as(unionDataPath.longitude),
                Expressions.as(null, unionDataPath.heartBeat),
                Expressions.as(null, unionDataPath.pressureMin),
                Expressions.as(null, unionDataPath.pressureMax),
                Expressions.as(null, unionDataPath.bloodOxygenLevel))
                .where(positionData.userSsn.in(ssnFilterFromPositionData)
                        .and(positionData.userSsn.in(ssnFilterFromHealthData)));
        Path joinData = Expressions.path(Void.class, "joinData");
        JPASQLQuery unionQuery = query().select(Wildcard.all).from(query()
                .union(healthQuery, positionQuery).as(joinData));
        JPASQLQuery<Double> query = query()
                .select(Expressions.numberOperation(Double.class,
                        AggregatorOperatorUtils.getSqlOperator(aggregatorOperator),
                        requestType.getFieldPath())).from(unionQuery, unionDataPath.alias)
                .join(user).on(user.ssn.eq(unionDataPath.userSsn));
        filters.stream().filter(filterStatement -> filterStatement.getColumn().contains(FieldType.getUserFields()))
                .forEach(predicateBuilder::addFilterStatement);
        List<Double> result = query.where(predicateBuilder.build()).fetch();
        return result.get(0);
    }

    private JPASQLQuery<?> query(){
        SQLTemplates templates = MySQLTemplates.builder().build();
        return new JPASQLQuery<>(entityManager, templates);
    }
}
