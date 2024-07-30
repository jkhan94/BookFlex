package com.sparta.bookflex.domain.coupon.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bookflex.domain.coupon.entity.Coupon;
import com.sparta.bookflex.domain.coupon.entity.UserCoupon;
import com.sparta.bookflex.domain.coupon.enums.CouponStatus;
import com.sparta.bookflex.domain.user.enums.UserGrade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static com.sparta.bookflex.domain.coupon.entity.QCoupon.coupon;
import static com.sparta.bookflex.domain.coupon.entity.QUserCoupon.userCoupon;

@RequiredArgsConstructor
public class CouponQRepositoryImpl implements CouponQRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Coupon> findAvailableByUserGrade(UserGrade grade, Pageable pageable) {
        List<Coupon> result = queryFactory.select(coupon)
                .from(coupon)
                .where(coupon.eligibleGrade.eq(grade)
                        .and(coupon.couponStatus.eq(CouponStatus.Available))
                        .and(coupon.totalCount.gt(0))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Coupon> count = queryFactory.select(coupon)
                .from(coupon)
                .where(coupon.eligibleGrade.eq(grade)
                        .and(coupon.couponStatus.eq(CouponStatus.Available))
                        .and(coupon.totalCount.gt(0))
                )
                .fetch();

        return new PageImpl<>(result, pageable, count.size());
    }

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
