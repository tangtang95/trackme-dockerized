package com.poianitibaldizhou.trackme.sharedataservice.entity.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QHealthData is a Querydsl query type for QHealthData
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QHealthData extends com.querydsl.sql.RelationalPathBase<QHealthData> {

    private static final long serialVersionUID = 1297795325;

    public static final QHealthData healthData = new QHealthData("health_data");

    public final NumberPath<Integer> bloodOxygenLevel = createNumber("bloodOxygenLevel", Integer.class);

    public final NumberPath<Integer> heartBeat = createNumber("heartBeat", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> pressureMax = createNumber("pressureMax", Integer.class);

    public final NumberPath<Integer> pressureMin = createNumber("pressureMin", Integer.class);

    public final DateTimePath<java.sql.Timestamp> timestamp = createDateTime("timestamp", java.sql.Timestamp.class);

    public final StringPath userSsn = createString("userSsn");

    public final com.querydsl.sql.PrimaryKey<QHealthData> primary = createPrimaryKey(id);

    public QHealthData(String variable) {
        super(QHealthData.class, forVariable(variable), "null", "health_data");
        addMetadata();
    }

    public QHealthData(String variable, String schema, String table) {
        super(QHealthData.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QHealthData(String variable, String schema) {
        super(QHealthData.class, forVariable(variable), schema, "health_data");
        addMetadata();
    }

    public QHealthData(Path<? extends QHealthData> path) {
        super(path.getType(), path.getMetadata(), "null", "health_data");
        addMetadata();
    }

    public QHealthData(PathMetadata metadata) {
        super(QHealthData.class, metadata, "null", "health_data");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(bloodOxygenLevel, ColumnMetadata.named("blood_oxygen_level").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(heartBeat, ColumnMetadata.named("heart_beat").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(pressureMax, ColumnMetadata.named("pressure_max").withIndex(4).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(pressureMin, ColumnMetadata.named("pressure_min").withIndex(5).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(timestamp, ColumnMetadata.named("timestamp").withIndex(6).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(userSsn, ColumnMetadata.named("user_ssn").withIndex(7).ofType(Types.VARCHAR).withSize(16).notNull());
    }

}

