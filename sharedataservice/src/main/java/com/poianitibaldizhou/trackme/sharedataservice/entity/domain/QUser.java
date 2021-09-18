package com.poianitibaldizhou.trackme.sharedataservice.entity.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QUser is a Querydsl query type for QUser
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QUser extends com.querydsl.sql.RelationalPathBase<QUser> {

    private static final long serialVersionUID = 1000081890;

    public static final QUser user = new QUser("user");

    public final StringPath birthCity = createString("birthCity");

    public final DatePath<java.sql.Date> birthDate = createDate("birthDate", java.sql.Date.class);

    public final StringPath birthNation = createString("birthNation");

    public final StringPath firstName = createString("firstName");

    public final StringPath lastName = createString("lastName");

    public final StringPath ssn = createString("ssn");

    public final com.querydsl.sql.PrimaryKey<QUser> primary = createPrimaryKey(ssn);

    public QUser(String variable) {
        super(QUser.class, forVariable(variable), "null", "user");
        addMetadata();
    }

    public QUser(String variable, String schema, String table) {
        super(QUser.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QUser(String variable, String schema) {
        super(QUser.class, forVariable(variable), schema, "user");
        addMetadata();
    }

    public QUser(Path<? extends QUser> path) {
        super(path.getType(), path.getMetadata(), "null", "user");
        addMetadata();
    }

    public QUser(PathMetadata metadata) {
        super(QUser.class, metadata, "null", "user");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(birthCity, ColumnMetadata.named("birth_city").withIndex(2).ofType(Types.VARCHAR).withSize(150).notNull());
        addMetadata(birthDate, ColumnMetadata.named("birth_date").withIndex(3).ofType(Types.DATE).withSize(10).notNull());
        addMetadata(birthNation, ColumnMetadata.named("birth_nation").withIndex(4).ofType(Types.VARCHAR).withSize(100).notNull());
        addMetadata(firstName, ColumnMetadata.named("first_name").withIndex(5).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(lastName, ColumnMetadata.named("last_name").withIndex(6).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(ssn, ColumnMetadata.named("ssn").withIndex(1).ofType(Types.VARCHAR).withSize(16).notNull());
    }

}

