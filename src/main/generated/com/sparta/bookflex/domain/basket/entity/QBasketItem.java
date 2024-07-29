package com.sparta.bookflex.domain.basket.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBasketItem is a Querydsl query type for BasketItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBasketItem extends EntityPathBase<BasketItem> {

    private static final long serialVersionUID = 1223564576L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBasketItem basketItem = new QBasketItem("basketItem");

    public final com.sparta.bookflex.common.utill.QTimestamped _super = new com.sparta.bookflex.common.utill.QTimestamped(this);

    public final QBasket basket;

    public final com.sparta.bookflex.domain.book.entity.QBook book;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<java.math.BigDecimal> price = createNumber("price", java.math.BigDecimal.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public QBasketItem(String variable) {
        this(BasketItem.class, forVariable(variable), INITS);
    }

    public QBasketItem(Path<? extends BasketItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBasketItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBasketItem(PathMetadata metadata, PathInits inits) {
        this(BasketItem.class, metadata, inits);
    }

    public QBasketItem(Class<? extends BasketItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.basket = inits.isInitialized("basket") ? new QBasket(forProperty("basket"), inits.get("basket")) : null;
        this.book = inits.isInitialized("book") ? new com.sparta.bookflex.domain.book.entity.QBook(forProperty("book"), inits.get("book")) : null;
    }

}

