package com.sparta.bookflex.domain.coupon.scheduler;

import com.sparta.bookflex.domain.coupon.repository.CouponRepository;
import com.sparta.bookflex.domain.coupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j(topic = "CouponScheduler")
@Component
@RequiredArgsConstructor
public class CouponScheduler {
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    @Scheduled(cron = "0 0 1 * * *") // 매일 오전 1시
    public void deleteExpiredCoupon() throws InterruptedException {
        log.info("만료된 쿠폰 삭제");
        couponRepository.deleteExpiredCoupon();
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 오전 12시
    public void updateIssuedCoupon() throws InterruptedException {
        log.info("발급 가능해진 쿠폰을 사용가능 상태로 변경");
        couponRepository.updateIssuedCoupon();
    }

}
