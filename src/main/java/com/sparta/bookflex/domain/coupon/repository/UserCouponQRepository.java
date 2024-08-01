package com.sparta.bookflex.domain.coupon.repository;

import com.sparta.bookflex.domain.coupon.entity.UserCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserCouponQRepository {
    Page<UserCoupon> findAllByUserId(long userId, Pageable pageable);

    List<UserCoupon> findAllByUserId(long userId);

    void deleteUserCoupon();

}

