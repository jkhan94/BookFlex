package com.sparta.bookflex.domain.payment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPayment is a Querydsl query type for Payment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPayment extends EntityPathBase<Payment> {

    private static final long serialVersionUID = -963052793L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPayment payment = new QPayment("payment");

    public final com.sparta.bookflex.common.utill.QTimestamped _super = new com.sparta.bookflex.common.utill.QTimestamped(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> Id = createNumber("Id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.sparta.bookflex.domain.orderbook.entity.QOrderBook orderBook;

    public final StringPath paymentMethod = createString("paymentMethod");

    public final NumberPath<java.math.BigDecimal> price = createNumber("price", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> refundAmount = createNumber("refundAmount", java.math.BigDecimal.class);

    public final EnumPath<com.sparta.bookflex.domain.payment.enums.PaymentStatus> status = createEnum("status", com.sparta.bookflex.domain.payment.enums.PaymentStatus.class);

    public QPayment(String variable) {
        this(Payment.class, forVariable(variable), INITS);
    }

    public QPayment(Path<? extends Payment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPayment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPayment(PathMetadata metadata, PathInits inits) {
        this(Payment.class, metadata, inits);
    }

    public QPayment(Class<? extends Payment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.orderBook = inits.isInitialized("orderBook") ? new com.sparta.bookflex.domain.orderbook.entity.QOrderBook(forProperty("orderBook"), inits.get("orderBook")) : null;
    }

}

