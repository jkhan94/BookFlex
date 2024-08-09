package com.sparta.bookflex.domain.systemlog.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTraceOfUserLog is a Querydsl query type for TraceOfUserLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTraceOfUserLog extends EntityPathBase<TraceOfUserLog> {

    private static final long serialVersionUID = 52695595L;

    public static final QTraceOfUserLog traceOfUserLog = new QTraceOfUserLog("traceOfUserLog");

    public final EnumPath<com.sparta.bookflex.domain.systemlog.enums.ActionType> activityType = createEnum("activityType", com.sparta.bookflex.domain.systemlog.enums.ActionType.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath username = createString("username");

    public QTraceOfUserLog(String variable) {
        super(TraceOfUserLog.class, forVariable(variable));
    }

    public QTraceOfUserLog(Path<? extends TraceOfUserLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTraceOfUserLog(PathMetadata metadata) {
        super(TraceOfUserLog.class, metadata);
    }

}

