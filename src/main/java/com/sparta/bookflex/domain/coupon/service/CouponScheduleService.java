package com.sparta.bookflex.domain.coupon.service;

import com.sparta.bookflex.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponScheduleService {

    private final CouponRepository couponRepository;

    public void deleteExpiredCoupon() {
        couponRepository.deleteExpiredCoupon();
    }

    public void updateIssuedCoupon() {
        couponRepository.updateIssuedCoupon();
    }

}
