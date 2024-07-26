package com.sparta.bookflex.domain.coupon.repository;

import com.sparta.bookflex.domain.coupon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponQRepository {
    Page<Coupon> findAllByUserId(long userId, Pageable pageable);

    void deleteExpiredCoupon();

    void updateIssuedCoupon();
}
