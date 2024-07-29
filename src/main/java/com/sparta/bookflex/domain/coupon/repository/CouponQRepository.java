package com.sparta.bookflex.domain.coupon.repository;

public interface CouponQRepository {
    void deleteExpiredCoupon();

    void updateIssuedCoupon();
}
