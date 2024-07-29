package com.sparta.bookflex.domain.systemlog.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSystemLog is a Querydsl query type for SystemLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSystemLog extends EntityPathBase<SystemLog> {

    private static final long serialVersionUID = 392878471L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSystemLog systemLog = new QSystemLog("systemLog");

    public final EnumPath<com.sparta.bookflex.domain.systemlog.enums.ActionType> action = createEnum("action", com.sparta.bookflex.domain.systemlog.enums.ActionType.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.sparta.bookflex.domain.user.entity.QUser user;

    public QSystemLog(String variable) {
        this(SystemLog.class, forVariable(variable), INITS);
    }

    public QSystemLog(Path<? extends SystemLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSystemLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSystemLog(PathMetadata metadata, PathInits inits) {
        this(SystemLog.class, metadata, inits);
    }

    public QSystemLog(Class<? extends SystemLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.sparta.bookflex.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

