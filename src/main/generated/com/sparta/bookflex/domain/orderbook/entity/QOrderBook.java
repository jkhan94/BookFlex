package com.sparta.bookflex.domain.orderbook.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderBook is a Querydsl query type for OrderBook
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderBook extends EntityPathBase<OrderBook> {

    private static final long serialVersionUID = 242031111L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderBook orderBook = new QOrderBook("orderBook");

    public final com.sparta.bookflex.common.utill.QTimestamped _super = new com.sparta.bookflex.common.utill.QTimestamped(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<java.math.BigDecimal> discount = createNumber("discount", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> discountPrice = createNumber("discountPrice", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> discountTotal = createNumber("discountTotal", java.math.BigDecimal.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isCoupon = createBoolean("isCoupon");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final ListPath<OrderItem, QOrderItem> orderItemList = this.<OrderItem, QOrderItem>createList("orderItemList", OrderItem.class, QOrderItem.class, PathInits.DIRECT2);

    public final StringPath orderNo = createString("orderNo");

    public final ListPath<com.sparta.bookflex.domain.sale.entity.Sale, com.sparta.bookflex.domain.sale.entity.QSale> saleList = this.<com.sparta.bookflex.domain.sale.entity.Sale, com.sparta.bookflex.domain.sale.entity.QSale>createList("saleList", com.sparta.bookflex.domain.sale.entity.Sale.class, com.sparta.bookflex.domain.sale.entity.QSale.class, PathInits.DIRECT2);

    public final EnumPath<com.sparta.bookflex.domain.orderbook.emuns.OrderState> status = createEnum("status", com.sparta.bookflex.domain.orderbook.emuns.OrderState.class);

    public final NumberPath<java.math.BigDecimal> total = createNumber("total", java.math.BigDecimal.class);

    public final com.sparta.bookflex.domain.user.entity.QUser user;

    public final com.sparta.bookflex.domain.coupon.entity.QUserCoupon userCoupon;

    public QOrderBook(String variable) {
        this(OrderBook.class, forVariable(variable), INITS);
    }

    public QOrderBook(Path<? extends OrderBook> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderBook(PathMetadata metadata, PathInits inits) {
        this(OrderBook.class, metadata, inits);
    }

    public QOrderBook(Class<? extends OrderBook> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.sparta.bookflex.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
        this.userCoupon = inits.isInitialized("userCoupon") ? new com.sparta.bookflex.domain.coupon.entity.QUserCoupon(forProperty("userCoupon"), inits.get("userCoupon")) : null;
    }

}

