package com.sparta.bookflex.domain.coupon.service;

import com.sparta.bookflex.domain.coupon.repository.CouponRepository;
import com.sparta.bookflex.domain.coupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponScheduleService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public void deleteExpiredCoupon() {
        couponRepository.deleteExpiredCoupon();
    }

    public void updateIssuedCoupon() {
        couponRepository.updateIssuedCoupon();
    }

    public void updateGradeCoupon() {
        userCouponRepository.updateGradeCoupon();
    }

    public void updateBirthdayCoupon() {
        userCouponRepository.updateBirthdayCoupon();
    }
}
