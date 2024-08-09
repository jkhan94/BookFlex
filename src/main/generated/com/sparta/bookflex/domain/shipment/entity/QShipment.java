package com.sparta.bookflex.domain.shipment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QShipment is a Querydsl query type for Shipment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShipment extends EntityPathBase<Shipment> {

    private static final long serialVersionUID = 1930405205L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QShipment shipment = new QShipment("shipment");

    public final com.sparta.bookflex.common.utill.QTimestamped _super = new com.sparta.bookflex.common.utill.QTimestamped(this);

    public final StringPath address = createString("address");

    public final StringPath carrier = createString("carrier");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deliveredAt = createDateTime("deliveredAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.sparta.bookflex.domain.orderbook.entity.QOrderBook orderBook;

    public final DateTimePath<java.time.LocalDateTime> shippedAt = createDateTime("shippedAt", java.time.LocalDateTime.class);

    public final StringPath trackingNumber = createString("trackingNumber");

    public final com.sparta.bookflex.domain.user.entity.QUser user;

    public QShipment(String variable) {
        this(Shipment.class, forVariable(variable), INITS);
    }

    public QShipment(Path<? extends Shipment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QShipment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QShipment(PathMetadata metadata, PathInits inits) {
        this(Shipment.class, metadata, inits);
    }

    public QShipment(Class<? extends Shipment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.orderBook = inits.isInitialized("orderBook") ? new com.sparta.bookflex.domain.orderbook.entity.QOrderBook(forProperty("orderBook"), inits.get("orderBook")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.bookflex.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

