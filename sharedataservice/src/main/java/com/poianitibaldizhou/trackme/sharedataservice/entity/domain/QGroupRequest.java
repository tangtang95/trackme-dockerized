package com.poianitibaldizhou.trackme.sharedataservice.entity.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QGroupRequest is a Querydsl query type for QGroupRequest
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QGroupRequest extends com.querydsl.sql.RelationalPathBase<QGroupRequest> {

    private static final long serialVersionUID = -1979175385;

    public static final QGroupRequest groupRequest = new QGroupRequest("group_request");

    public final StringPath aggregatorOperator = createString("aggregatorOperator");

    public final DateTimePath<java.sql.Timestamp> creationTimestamp = createDateTime("creationTimestamp", java.sql.Timestamp.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath requestType = createString("requestType");

    public final NumberPath<Long> thirdPartyId = createNumber("thirdPartyId", Long.class);

    public final com.querydsl.sql.PrimaryKey<QGroupRequest> primary = createPrimaryKey(id);

    public QGroupRequest(String variable) {
        super(QGroupRequest.class, forVariable(variable), "null", "group_request");
        addMetadata();
    }

    public QGroupRequest(String variable, String schema, String table) {
        super(QGroupRequest.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QGroupRequest(String variable, String schema) {
        super(QGroupRequest.class, forVariable(variable), schema, "group_request");
        addMetadata();
    }

    public QGroupRequest(Path<? extends QGroupRequest> path) {
        super(path.getType(), path.getMetadata(), "null", "group_request");
        addMetadata();
    }

    public QGroupRequest(PathMetadata metadata) {
        super(QGroupRequest.class, metadata, "null", "group_request");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(aggregatorOperator, ColumnMetadata.named("aggregator_operator").withIndex(2).ofType(Types.VARCHAR).withSize(20).notNull());
        addMetadata(creationTimestamp, ColumnMetadata.named("creation_timestamp").withIndex(3).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(requestType, ColumnMetadata.named("request_type").withIndex(4).ofType(Types.VARCHAR).withSize(20).notNull());
        addMetadata(thirdPartyId, ColumnMetadata.named("third_party_id").withIndex(5).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

