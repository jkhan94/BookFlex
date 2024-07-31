package com.sparta.bookflex.domain.coupon.repository;

import com.sparta.bookflex.domain.coupon.entity.Coupon;
import com.sparta.bookflex.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponQRepository {
    Page<Coupon> findAvailableByUserGrade(User user, Pageable pageable);

    void deleteExpiredCoupon();

    void updateIssuedCoupon();

    void updateGradeCoupon();

    void updateBirthdayCoupon();

}
