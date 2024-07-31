package com.sparta.bookflex.domain.systemlog.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSystemLog is a Querydsl query type for SystemLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSystemLog extends EntityPathBase<SystemLog> {

    private static final long serialVersionUID = 392878471L;

    public static final QSystemLog systemLog = new QSystemLog("systemLog");

    public final EnumPath<com.sparta.bookflex.domain.systemlog.enums.ActionType> action = createEnum("action", com.sparta.bookflex.domain.systemlog.enums.ActionType.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath username = createString("username");

    public QSystemLog(String variable) {
        super(SystemLog.class, forVariable(variable));
    }

    public QSystemLog(Path<? extends SystemLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSystemLog(PathMetadata metadata) {
        super(SystemLog.class, metadata);
    }

}

