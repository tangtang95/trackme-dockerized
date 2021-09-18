package com.poianitibaldizhou.trackme.sharedataservice.entity.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QHibernateSequence is a Querydsl query type for QHibernateSequence
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QHibernateSequence extends com.querydsl.sql.RelationalPathBase<QHibernateSequence> {

    private static final long serialVersionUID = -1182416516;

    public static final QHibernateSequence hibernateSequence = new QHibernateSequence("hibernate_sequence");

    public final NumberPath<Long> nextVal = createNumber("nextVal", Long.class);

    public QHibernateSequence(String variable) {
        super(QHibernateSequence.class, forVariable(variable), "null", "hibernate_sequence");
        addMetadata();
    }

    public QHibernateSequence(String variable, String schema, String table) {
        super(QHibernateSequence.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QHibernateSequence(String variable, String schema) {
        super(QHibernateSequence.class, forVariable(variable), schema, "hibernate_sequence");
        addMetadata();
    }

    public QHibernateSequence(Path<? extends QHibernateSequence> path) {
        super(path.getType(), path.getMetadata(), "null", "hibernate_sequence");
        addMetadata();
    }

    public QHibernateSequence(PathMetadata metadata) {
        super(QHibernateSequence.class, metadata, "null", "hibernate_sequence");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(nextVal, ColumnMetadata.named("next_val").withIndex(1).ofType(Types.BIGINT).withSize(19));
    }

}

