package com.sparta.bookflex.domain.coupon.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bookflex.domain.coupon.entity.UserCoupon;
import com.sparta.bookflex.domain.coupon.enums.CouponType;
import com.sparta.bookflex.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

import static com.sparta.bookflex.domain.coupon.entity.QCoupon.coupon;
import static com.sparta.bookflex.domain.coupon.entity.QUserCoupon.userCoupon;
import static com.sparta.bookflex.domain.user.entity.QUser.user;

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

    @Override
    @Transactional
    public void updateGradeCoupon() {
        List<User> count = queryFactory.select(user).from(user).fetch();

        LocalDate start = YearMonth.now().atDay(1);
        LocalDate end = YearMonth.now().atEndOfMonth();

        queryFactory.update(coupon)
                .set(coupon.totalCount, Integer.MAX_VALUE)
                .set(coupon.startDate, start.atStartOfDay())
                .set(coupon.expirationDate, end.atTime(LocalTime.MAX))
                .where(coupon.couponType.eq(CouponType.GRADE))
                .execute();
    }

    @Override
    @Transactional
    public void updateBirthdayCoupon() {
        List<User> count = queryFactory.select(user).from(user).where(user.birthDay.isNotNull()).fetch();

        LocalDate start = YearMonth.now().atDay(1);
        LocalDate end = YearMonth.now().atEndOfMonth();

        queryFactory.update(coupon)
                .set(coupon.totalCount, Integer.MAX_VALUE)
                .set(coupon.startDate, start.atStartOfDay())
                .set(coupon.expirationDate, end.atTime(LocalTime.MAX))
                .where(coupon.couponType.eq(CouponType.BIRTHDAY))
                .execute();
    }
}
