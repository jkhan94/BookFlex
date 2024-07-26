package com.sparta.bookflex.domain.coupon.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bookflex.domain.coupon.entity.Coupon;
import com.sparta.bookflex.domain.coupon.enums.CouponStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static com.sparta.bookflex.domain.coupon.entity.QCoupon.coupon;
import static com.sparta.bookflex.domain.coupon.entity.QUserCoupon.userCoupon;

@RequiredArgsConstructor
public class CouponQRepositoryImpl implements CouponQRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteExpiredCoupon() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        queryFactory.delete(coupon)
                .where(coupon.expirationDate.before(now))
                .execute();
    }

    @Override
    public void updateIssuedCoupon() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        queryFactory.update(coupon)
                .set(coupon.couponStatus, CouponStatus.Available)
                .where(coupon.startDate.after(now))
                .execute();
    }
}
