package com.sparta.bookflex.domain.coupon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCoupon is a Querydsl query type for Coupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoupon extends EntityPathBase<Coupon> {

    private static final long serialVersionUID = 1504887085L;

    public static final QCoupon coupon = new QCoupon("coupon");

    public final com.sparta.bookflex.common.utill.QTimestamped _super = new com.sparta.bookflex.common.utill.QTimestamped(this);

    public final StringPath couponName = createString("couponName");

    public final EnumPath<com.sparta.bookflex.domain.coupon.enums.CouponStatus> couponStatus = createEnum("couponStatus", com.sparta.bookflex.domain.coupon.enums.CouponStatus.class);

    public final EnumPath<com.sparta.bookflex.domain.coupon.enums.CouponType> couponType = createEnum("couponType", com.sparta.bookflex.domain.coupon.enums.CouponType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<java.math.BigDecimal> discountPrice = createNumber("discountPrice", java.math.BigDecimal.class);

    public final EnumPath<com.sparta.bookflex.domain.coupon.enums.DiscountType> discountType = createEnum("discountType", com.sparta.bookflex.domain.coupon.enums.DiscountType.class);

    public final EnumPath<com.sparta.bookflex.domain.user.enums.UserGrade> eligibleGrade = createEnum("eligibleGrade", com.sparta.bookflex.domain.user.enums.UserGrade.class);

    public final DateTimePath<java.time.LocalDateTime> expirationDate = createDateTime("expirationDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> minPrice = createNumber("minPrice", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> totalCount = createNumber("totalCount", Integer.class);

    public final NumberPath<Integer> validityDays = createNumber("validityDays", Integer.class);

    public QCoupon(String variable) {
        super(Coupon.class, forVariable(variable));
    }

    public QCoupon(Path<? extends Coupon> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCoupon(PathMetadata metadata) {
        super(Coupon.class, metadata);
    }

}

