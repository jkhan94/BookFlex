package com.sparta.bookflex.domain.orderbook.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderItemRefund is a Querydsl query type for OrderItemRefund
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderItemRefund extends EntityPathBase<OrderItemRefund> {

    private static final long serialVersionUID = 486735113L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderItemRefund orderItemRefund = new QOrderItemRefund("orderItemRefund");

    public final com.sparta.bookflex.common.utill.QTimestamped _super = new com.sparta.bookflex.common.utill.QTimestamped(this);

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final QOrderItem orderItem;

    public final EnumPath<com.sparta.bookflex.domain.orderbook.emuns.RefundStatus> refundStatus = createEnum("refundStatus", com.sparta.bookflex.domain.orderbook.emuns.RefundStatus.class);

    public QOrderItemRefund(String variable) {
        this(OrderItemRefund.class, forVariable(variable), INITS);
    }

    public QOrderItemRefund(Path<? extends OrderItemRefund> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderItemRefund(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderItemRefund(PathMetadata metadata, PathInits inits) {
        this(OrderItemRefund.class, metadata, inits);
    }

    public QOrderItemRefund(Class<? extends OrderItemRefund> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.orderItem = inits.isInitialized("orderItem") ? new QOrderItem(forProperty("orderItem"), inits.get("orderItem")) : null;
    }

}

