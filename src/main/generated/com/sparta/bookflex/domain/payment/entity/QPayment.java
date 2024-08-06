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

    public final StringPath cancelReason = createString("cancelReason");

    public final BooleanPath cancelYN = createBoolean("cancelYN");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<java.math.BigDecimal> discount = createNumber("discount", java.math.BigDecimal.class);

    public final StringPath failReason = createString("failReason");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.sparta.bookflex.domain.orderbook.entity.QOrderBook orderBook;

    public final BooleanPath paySuccessYN = createBoolean("paySuccessYN");

    public final StringPath payToken = createString("payToken");

    public final EnumPath<com.sparta.bookflex.domain.payment.enums.PayType> payType = createEnum("payType", com.sparta.bookflex.domain.payment.enums.PayType.class);

    public final EnumPath<com.sparta.bookflex.domain.payment.enums.PaymentStatus> status = createEnum("status", com.sparta.bookflex.domain.payment.enums.PaymentStatus.class);

    public final NumberPath<Integer> total = createNumber("total", Integer.class);

    public final com.sparta.bookflex.domain.user.entity.QUser user;

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
        this.user = inits.isInitialized("user") ? new com.sparta.bookflex.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

