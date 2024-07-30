package com.sparta.bookflex.domain.coupon.repository;

import com.sparta.bookflex.domain.coupon.entity.UserCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCouponQRepository {
    Page<UserCoupon> findAllByUserId(long userId, Pageable pageable);

    void updateGradeCoupon();

    void updateBirthdayCoupon();
}

