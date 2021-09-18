package com.poianitibaldizhou.trackme.sharedataservice.entity.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QPositionData is a Querydsl query type for QPositionData
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QPositionData extends com.querydsl.sql.RelationalPathBase<QPositionData> {

    private static final long serialVersionUID = 1603098506;

    public static final QPositionData positionData = new QPositionData("position_data");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final DateTimePath<java.sql.Timestamp> timestamp = createDateTime("timestamp", java.sql.Timestamp.class);

    public final StringPath userSsn = createString("userSsn");

    public final com.querydsl.sql.PrimaryKey<QPositionData> primary = createPrimaryKey(id);

    public QPositionData(String variable) {
        super(QPositionData.class, forVariable(variable), "null", "position_data");
        addMetadata();
    }

    public QPositionData(String variable, String schema, String table) {
        super(QPositionData.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QPositionData(String variable, String schema) {
        super(QPositionData.class, forVariable(variable), schema, "position_data");
        addMetadata();
    }

    public QPositionData(Path<? extends QPositionData> path) {
        super(path.getType(), path.getMetadata(), "null", "position_data");
        addMetadata();
    }

    public QPositionData(PathMetadata metadata) {
        super(QPositionData.class, metadata, "null", "position_data");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(latitude, ColumnMetadata.named("latitude").withIndex(2).ofType(Types.DOUBLE).withSize(22).notNull());
        addMetadata(longitude, ColumnMetadata.named("longitude").withIndex(3).ofType(Types.DOUBLE).withSize(22).notNull());
        addMetadata(timestamp, ColumnMetadata.named("timestamp").withIndex(4).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(userSsn, ColumnMetadata.named("user_ssn").withIndex(5).ofType(Types.VARCHAR).withSize(16).notNull());
    }

}

