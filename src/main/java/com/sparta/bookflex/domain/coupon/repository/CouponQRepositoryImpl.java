package com.sparta.bookflex.domain.coupon.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bookflex.domain.coupon.enums.CouponStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.sparta.bookflex.domain.coupon.entity.QCoupon.coupon;

@RequiredArgsConstructor
public class CouponQRepositoryImpl implements CouponQRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional
    public void deleteExpiredCoupon() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        queryFactory.delete(coupon)
                .where(coupon.expirationDate.before(now))
                .execute();
    }

    @Override
    @Transactional
    public void updateIssuedCoupon() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        queryFactory.update(coupon)
                .set(coupon.couponStatus, CouponStatus.Available)
                .where(coupon.startDate.before(now))
                .execute();
    }
}
