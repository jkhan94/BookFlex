package com.sparta.bookflex.domain.coupon.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bookflex.domain.coupon.entity.Coupon;
import com.sparta.bookflex.domain.coupon.enums.CouponStatus;
import com.sparta.bookflex.domain.coupon.enums.CouponType;
import com.sparta.bookflex.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

import static com.sparta.bookflex.domain.coupon.entity.QCoupon.coupon;

@RequiredArgsConstructor
public class CouponQRepositoryImpl implements CouponQRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Coupon> findAvailableByUserGrade(User user, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(coupon.eligibleGrade.eq(user.getGrade()));
        builder.and(coupon.couponStatus.eq(CouponStatus.Available));
        builder.and(coupon.totalCount.gt(0));

        List<Coupon> births = queryFactory.select(coupon)
                .from(coupon)
                .where(coupon.couponType.eq(CouponType.BIRTHDAY))
                .fetch();

        if (user.getBirthDay() == null) {
            builder.and(coupon.couponType.ne(CouponType.BIRTHDAY));
        } else {
            for (Coupon birth : births) {
                if (user.getBirthDay().getMonthValue() != birth.getStartDate().getMonthValue()) {
                    builder.and(coupon.id.ne(birth.getId()));
                }
            }
        }

        List<Coupon> grades = queryFactory.select(coupon)
                .from(coupon)
                .where(coupon.couponType.eq(CouponType.GRADE))
                .fetch();

        for (Coupon grade : grades) {
            if (grade.getStartDate().isAfter(LocalDateTime.now())) {
                builder.and(coupon.id.ne(grade.getId()));
            }
        }

        List<Coupon> result = queryFactory.select(coupon)
                .from(coupon)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Coupon> count = queryFactory.select(coupon)
                .from(coupon)
                .where(builder)
                .fetch();

        return new PageImpl<>(result, pageable, count.size());
    }

    @Override
    @Transactional
    public void deleteExpiredCoupon() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        queryFactory.delete(coupon)
                .where(coupon.expirationDate.before(now)
                        .and(coupon.couponType.notIn(CouponType.BIRTHDAY, CouponType.GRADE)))
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

    @Override
    @Transactional
    public void updateGradeCoupon() {
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
