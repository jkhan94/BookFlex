package com.sparta.bookflex.domain.coupon.service;

import com.sparta.bookflex.domain.coupon.entity.Coupon;
import com.sparta.bookflex.domain.coupon.enums.CouponType;
import com.sparta.bookflex.domain.coupon.repository.CouponRepository;
import com.sparta.bookflex.domain.coupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponScheduleService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final CouponService couponService;

    public void deleteExpiredCoupon() {
        couponRepository.deleteExpiredCoupon();
    }

    public void updateIssuedCoupon() {
        couponRepository.updateIssuedCoupon();
    }

    public void updateGradeCoupon() {
        userCouponRepository.deleteUserCoupon();
        couponRepository.updateGradeCoupon();
        List<Coupon> coupons = couponRepository.findByCouponType(CouponType.GRADE);
        for (Coupon coupon : coupons) {
            couponService.issueCouponToAll(coupon.getId());
        }
    }

    public void updateBirthdayCoupon() {
        userCouponRepository.deleteUserCoupon();
        couponRepository.updateBirthdayCoupon();
        List<Coupon> coupons = couponRepository.findByCouponType(CouponType.BIRTHDAY);
        for (Coupon coupon : coupons) {
            couponService.issueCouponToAll(coupon.getId());
        }
    }

}
