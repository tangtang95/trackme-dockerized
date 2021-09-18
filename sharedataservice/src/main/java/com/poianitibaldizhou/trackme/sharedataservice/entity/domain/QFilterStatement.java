package com.poianitibaldizhou.trackme.sharedataservice.entity.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QFilterStatement is a Querydsl query type for QFilterStatement
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QFilterStatement extends com.querydsl.sql.RelationalPathBase<QFilterStatement> {

    private static final long serialVersionUID = -1256988064;

    public static final QFilterStatement filterStatement = new QFilterStatement("filter_statement");

    public final StringPath comparisonSymbol = createString("comparisonSymbol");

    public final StringPath filterColumn = createString("filterColumn");

    public final NumberPath<Long> groupRequestId = createNumber("groupRequestId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath value = createString("value");

    public final com.querydsl.sql.PrimaryKey<QFilterStatement> primary = createPrimaryKey(id);

    public QFilterStatement(String variable) {
        super(QFilterStatement.class, forVariable(variable), "null", "filter_statement");
        addMetadata();
    }

    public QFilterStatement(String variable, String schema, String table) {
        super(QFilterStatement.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QFilterStatement(String variable, String schema) {
        super(QFilterStatement.class, forVariable(variable), schema, "filter_statement");
        addMetadata();
    }

    public QFilterStatement(Path<? extends QFilterStatement> path) {
        super(path.getType(), path.getMetadata(), "null", "filter_statement");
        addMetadata();
    }

    public QFilterStatement(PathMetadata metadata) {
        super(QFilterStatement.class, metadata, "null", "filter_statement");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(comparisonSymbol, ColumnMetadata.named("comparison_symbol").withIndex(3).ofType(Types.VARCHAR).withSize(10).notNull());
        addMetadata(filterColumn, ColumnMetadata.named("filter_column").withIndex(2).ofType(Types.VARCHAR).withSize(20).notNull());
        addMetadata(groupRequestId, ColumnMetadata.named("group_request_id").withIndex(5).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(value, ColumnMetadata.named("value").withIndex(4).ofType(Types.VARCHAR).withSize(50).notNull());
    }

}

