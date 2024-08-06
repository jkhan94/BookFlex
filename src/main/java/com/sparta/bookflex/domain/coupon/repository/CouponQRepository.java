package com.sparta.bookflex.domain.coupon.repository;

import com.sparta.bookflex.domain.coupon.entity.Coupon;
import com.sparta.bookflex.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CouponQRepository {
    Page<Coupon> findAvailableByUserGrade(User user, Pageable pageable, List<Long> issuedCouponIds);

    void updateIssueExpiredCoupon();

    void updateIssuedCoupon();

    void updateGradeCoupon();

    void updateBirthdayCoupon();

}
