package com.sparta.bookflex.domain.coupon.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bookflex.domain.coupon.entity.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.sparta.bookflex.domain.coupon.entity.QCoupon.coupon;
import static com.sparta.bookflex.domain.coupon.entity.QUserCoupon.userCoupon;

@RequiredArgsConstructor
public class UserCouponQRepositoryImpl implements UserCouponQRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserCoupon> findAllByUserId(long userId, Pageable pageable) {
        List<UserCoupon> result = queryFactory.select(userCoupon)
                .from(userCoupon)
                .join(coupon).on(coupon.id.eq(userCoupon.coupon.id))
                .where(userCoupon.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<UserCoupon> count = queryFactory.select(userCoupon)
                .from(userCoupon)
                .join(coupon).on(coupon.id.eq(userCoupon.coupon.id))
                .where(userCoupon.user.id.eq(userId))
                .fetch();

        return new PageImpl<>(result, pageable, count.size());
    }
}
