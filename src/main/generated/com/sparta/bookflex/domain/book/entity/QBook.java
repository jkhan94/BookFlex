package com.sparta.bookflex.domain.book.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = 356421875L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBook book = new QBook("book");

    public final com.sparta.bookflex.common.utill.QTimestamped _super = new com.sparta.bookflex.common.utill.QTimestamped(this);

    public final StringPath author = createString("author");

    public final ListPath<com.sparta.bookflex.domain.basket.entity.BasketItem, com.sparta.bookflex.domain.basket.entity.QBasketItem> baskeItemtList = this.<com.sparta.bookflex.domain.basket.entity.BasketItem, com.sparta.bookflex.domain.basket.entity.QBasketItem>createList("baskeItemtList", com.sparta.bookflex.domain.basket.entity.BasketItem.class, com.sparta.bookflex.domain.basket.entity.QBasketItem.class, PathInits.DIRECT2);

    public final StringPath bookDescription = createString("bookDescription");

    public final StringPath bookName = createString("bookName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.sparta.bookflex.domain.category.enums.Category> mainCategory = createEnum("mainCategory", com.sparta.bookflex.domain.category.enums.Category.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final ListPath<com.sparta.bookflex.domain.orderbook.entity.OrderItem, com.sparta.bookflex.domain.orderbook.entity.QOrderItem> orderItemList = this.<com.sparta.bookflex.domain.orderbook.entity.OrderItem, com.sparta.bookflex.domain.orderbook.entity.QOrderItem>createList("orderItemList", com.sparta.bookflex.domain.orderbook.entity.OrderItem.class, com.sparta.bookflex.domain.orderbook.entity.QOrderItem.class, PathInits.DIRECT2);

    public final com.sparta.bookflex.domain.photoimage.entity.QPhotoImage photoImage;

    public final NumberPath<java.math.BigDecimal> price = createNumber("price", java.math.BigDecimal.class);

    public final StringPath publisher = createString("publisher");

    public final ListPath<com.sparta.bookflex.domain.reveiw.entity.Review, com.sparta.bookflex.domain.reveiw.entity.QReview> reviewList = this.<com.sparta.bookflex.domain.reveiw.entity.Review, com.sparta.bookflex.domain.reveiw.entity.QReview>createList("reviewList", com.sparta.bookflex.domain.reveiw.entity.Review.class, com.sparta.bookflex.domain.reveiw.entity.QReview.class, PathInits.DIRECT2);

    public final ListPath<com.sparta.bookflex.domain.sale.entity.Sale, com.sparta.bookflex.domain.sale.entity.QSale> saleList = this.<com.sparta.bookflex.domain.sale.entity.Sale, com.sparta.bookflex.domain.sale.entity.QSale>createList("saleList", com.sparta.bookflex.domain.sale.entity.Sale.class, com.sparta.bookflex.domain.sale.entity.QSale.class, PathInits.DIRECT2);

    public final EnumPath<BookStatus> status = createEnum("status", BookStatus.class);

    public final NumberPath<Integer> stock = createNumber("stock", Integer.class);

    public final EnumPath<com.sparta.bookflex.domain.category.enums.Category> subCategory = createEnum("subCategory", com.sparta.bookflex.domain.category.enums.Category.class);

    public final ListPath<com.sparta.bookflex.domain.wish.entity.Wish, com.sparta.bookflex.domain.wish.entity.QWish> wishList = this.<com.sparta.bookflex.domain.wish.entity.Wish, com.sparta.bookflex.domain.wish.entity.QWish>createList("wishList", com.sparta.bookflex.domain.wish.entity.Wish.class, com.sparta.bookflex.domain.wish.entity.QWish.class, PathInits.DIRECT2);

    public QBook(String variable) {
        this(Book.class, forVariable(variable), INITS);
    }

    public QBook(Path<? extends Book> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBook(PathMetadata metadata, PathInits inits) {
        this(Book.class, metadata, inits);
    }

    public QBook(Class<? extends Book> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.photoImage = inits.isInitialized("photoImage") ? new com.sparta.bookflex.domain.photoimage.entity.QPhotoImage(forProperty("photoImage")) : null;
    }

}

