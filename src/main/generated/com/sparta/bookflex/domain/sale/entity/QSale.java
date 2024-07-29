package com.sparta.bookflex.domain.sale.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSale is a Querydsl query type for Sale
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSale extends EntityPathBase<Sale> {

    private static final long serialVersionUID = -380993873L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSale sale = new QSale("sale");

    public final com.sparta.bookflex.common.utill.QTimestamped _super = new com.sparta.bookflex.common.utill.QTimestamped(this);

    public final com.sparta.bookflex.domain.book.entity.QBook book;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.sparta.bookflex.domain.orderbook.entity.QOrderBook orderBook;

    public final NumberPath<java.math.BigDecimal> price = createNumber("price", java.math.BigDecimal.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final StringPath saleDate = createString("saleDate");

    public final EnumPath<com.sparta.bookflex.domain.orderbook.emuns.OrderState> status = createEnum("status", com.sparta.bookflex.domain.orderbook.emuns.OrderState.class);

    public final NumberPath<java.math.BigDecimal> total = createNumber("total", java.math.BigDecimal.class);

    public final com.sparta.bookflex.domain.user.entity.QUser user;

    public QSale(String variable) {
        this(Sale.class, forVariable(variable), INITS);
    }

    public QSale(Path<? extends Sale> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSale(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSale(PathMetadata metadata, PathInits inits) {
        this(Sale.class, metadata, inits);
    }

    public QSale(Class<? extends Sale> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new com.sparta.bookflex.domain.book.entity.QBook(forProperty("book"), inits.get("book")) : null;
        this.orderBook = inits.isInitialized("orderBook") ? new com.sparta.bookflex.domain.orderbook.entity.QOrderBook(forProperty("orderBook"), inits.get("orderBook")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.bookflex.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

