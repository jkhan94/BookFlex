package com.sparta.bookflex.domain.coupon.scheduler;

import com.sparta.bookflex.domain.coupon.service.CouponScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j(topic = "CouponScheduler")
@Component
@RequiredArgsConstructor
public class CouponScheduler {
    private final CouponScheduleService couponScheduleService;

    @Scheduled(cron = "0 0 0/1 * * *") // 1시간마다
    public void deleteExpiredCoupon() throws InterruptedException {
        log.info("만료된 쿠폰 삭제");
        couponScheduleService.deleteExpiredCoupon();
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 오전 12시
    public void updateIssuedCoupon() throws InterruptedException {
        log.info("발급 가능해진 쿠폰을 사용가능 상태로 변경");
        couponScheduleService.updateIssuedCoupon();
    }

    @Scheduled(cron = "0 0 0 * * *") // 매달 오전 12시
    public void updateGradeCoupon() throws InterruptedException {
        log.info("등급 쿠폰 수량, 발급시작일, 발급만료일 변경");
        couponScheduleService.updateGradeCoupon();
    }

    @Scheduled(cron = "0 0 0 * * *") // 매달 오전 12시
    public void updateBirthdayCoupon() throws InterruptedException {
        log.info("생일 쿠폰 수량, 발급시작일, 발급만료일 변경");
        couponScheduleService.updateBirthdayCoupon();
    }

}
