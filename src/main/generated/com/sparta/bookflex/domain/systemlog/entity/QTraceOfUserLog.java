package com.sparta.bookflex.domain.systemlog.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTraceOfUserLog is a Querydsl query type for TraceOfUserLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTraceOfUserLog extends EntityPathBase<TraceOfUserLog> {

    private static final long serialVersionUID = 52695595L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTraceOfUserLog traceOfUserLog = new QTraceOfUserLog("traceOfUserLog");

    public final EnumPath<com.sparta.bookflex.domain.systemlog.enums.ActionType> activityType = createEnum("activityType", com.sparta.bookflex.domain.systemlog.enums.ActionType.class);

    public final com.sparta.bookflex.domain.book.entity.QBook book;

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.sparta.bookflex.domain.user.entity.QUser user;

    public QTraceOfUserLog(String variable) {
        this(TraceOfUserLog.class, forVariable(variable), INITS);
    }

    public QTraceOfUserLog(Path<? extends TraceOfUserLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTraceOfUserLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTraceOfUserLog(PathMetadata metadata, PathInits inits) {
        this(TraceOfUserLog.class, metadata, inits);
    }

    public QTraceOfUserLog(Class<? extends TraceOfUserLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new com.sparta.bookflex.domain.book.entity.QBook(forProperty("book"), inits.get("book")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.bookflex.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

