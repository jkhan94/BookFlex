package com.sparta.bookflex.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -311185481L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final com.sparta.bookflex.common.utill.QTimestamped _super = new com.sparta.bookflex.common.utill.QTimestamped(this);

    public final StringPath address = createString("address");

    public final EnumPath<com.sparta.bookflex.domain.user.enums.RoleType> auth = createEnum("auth", com.sparta.bookflex.domain.user.enums.RoleType.class);

    public final com.sparta.bookflex.domain.basket.entity.QBasket basket;

    public final DatePath<java.time.LocalDate> birthDay = createDate("birthDay", java.time.LocalDate.class);

    public final ListPath<com.sparta.bookflex.domain.systemlog.entity.TraceOfUserLog, com.sparta.bookflex.domain.systemlog.entity.QTraceOfUserLog> copyOfSystemLogList = this.<com.sparta.bookflex.domain.systemlog.entity.TraceOfUserLog, com.sparta.bookflex.domain.systemlog.entity.QTraceOfUserLog>createList("copyOfSystemLogList", com.sparta.bookflex.domain.systemlog.entity.TraceOfUserLog.class, com.sparta.bookflex.domain.systemlog.entity.QTraceOfUserLog.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final EnumPath<com.sparta.bookflex.domain.user.enums.UserGrade> grade = createEnum("grade", com.sparta.bookflex.domain.user.enums.UserGrade.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> kakaoId = createNumber("kakaoId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final ListPath<com.sparta.bookflex.domain.orderbook.entity.OrderBook, com.sparta.bookflex.domain.orderbook.entity.QOrderBook> orderBookList = this.<com.sparta.bookflex.domain.orderbook.entity.OrderBook, com.sparta.bookflex.domain.orderbook.entity.QOrderBook>createList("orderBookList", com.sparta.bookflex.domain.orderbook.entity.OrderBook.class, com.sparta.bookflex.domain.orderbook.entity.QOrderBook.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final ListPath<com.sparta.bookflex.domain.qna.entity.Qna, com.sparta.bookflex.domain.qna.entity.QQna> qnaList = this.<com.sparta.bookflex.domain.qna.entity.Qna, com.sparta.bookflex.domain.qna.entity.QQna>createList("qnaList", com.sparta.bookflex.domain.qna.entity.Qna.class, com.sparta.bookflex.domain.qna.entity.QQna.class, PathInits.DIRECT2);

    public final StringPath refreshToken = createString("refreshToken");

    public final ListPath<com.sparta.bookflex.domain.reveiw.entity.Review, com.sparta.bookflex.domain.reveiw.entity.QReview> reviewList = this.<com.sparta.bookflex.domain.reveiw.entity.Review, com.sparta.bookflex.domain.reveiw.entity.QReview>createList("reviewList", com.sparta.bookflex.domain.reveiw.entity.Review.class, com.sparta.bookflex.domain.reveiw.entity.QReview.class, PathInits.DIRECT2);

    public final ListPath<com.sparta.bookflex.domain.sale.entity.Sale, com.sparta.bookflex.domain.sale.entity.QSale> saleList = this.<com.sparta.bookflex.domain.sale.entity.Sale, com.sparta.bookflex.domain.sale.entity.QSale>createList("saleList", com.sparta.bookflex.domain.sale.entity.Sale.class, com.sparta.bookflex.domain.sale.entity.QSale.class, PathInits.DIRECT2);

    public final ListPath<com.sparta.bookflex.domain.shipment.entity.Shipment, com.sparta.bookflex.domain.shipment.entity.QShipment> shipmentList = this.<com.sparta.bookflex.domain.shipment.entity.Shipment, com.sparta.bookflex.domain.shipment.entity.QShipment>createList("shipmentList", com.sparta.bookflex.domain.shipment.entity.Shipment.class, com.sparta.bookflex.domain.shipment.entity.QShipment.class, PathInits.DIRECT2);

    public final EnumPath<com.sparta.bookflex.domain.user.enums.UserState> state = createEnum("state", com.sparta.bookflex.domain.user.enums.UserState.class);

    public final ListPath<com.sparta.bookflex.domain.coupon.entity.UserCoupon, com.sparta.bookflex.domain.coupon.entity.QUserCoupon> userCouponList = this.<com.sparta.bookflex.domain.coupon.entity.UserCoupon, com.sparta.bookflex.domain.coupon.entity.QUserCoupon>createList("userCouponList", com.sparta.bookflex.domain.coupon.entity.UserCoupon.class, com.sparta.bookflex.domain.coupon.entity.QUserCoupon.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public final ListPath<Role, QRole> userRole = this.<Role, QRole>createList("userRole", Role.class, QRole.class, PathInits.DIRECT2);

    public final ListPath<com.sparta.bookflex.domain.wish.entity.Wish, com.sparta.bookflex.domain.wish.entity.QWish> wishList = this.<com.sparta.bookflex.domain.wish.entity.Wish, com.sparta.bookflex.domain.wish.entity.QWish>createList("wishList", com.sparta.bookflex.domain.wish.entity.Wish.class, com.sparta.bookflex.domain.wish.entity.QWish.class, PathInits.DIRECT2);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.basket = inits.isInitialized("basket") ? new com.sparta.bookflex.domain.basket.entity.QBasket(forProperty("basket"), inits.get("basket")) : null;
    }

}

