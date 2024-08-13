package com.sparta.bookflex.domain.coupon.service;

import com.sparta.bookflex.domain.coupon.entity.Coupon;
import com.sparta.bookflex.domain.coupon.entity.UserCoupon;
import com.sparta.bookflex.domain.coupon.enums.CouponStatus;
import com.sparta.bookflex.domain.coupon.enums.CouponType;
import com.sparta.bookflex.domain.coupon.enums.DiscountType;
import com.sparta.bookflex.domain.coupon.repository.CouponRepository;
import com.sparta.bookflex.domain.coupon.repository.UserCouponRepository;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.enums.RoleType;
import com.sparta.bookflex.domain.user.enums.UserGrade;
import com.sparta.bookflex.domain.user.enums.UserState;
import com.sparta.bookflex.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Test
    @DisplayName("쿠폰 발급 동시성 문제")
    void issueCouponConcurrently() throws InterruptedException {
        // given
        int couponQuantity = 10; // 최대 발급 개수
        int tryCouponIssue = 15; // 시도할 횟수
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        List<User> testUsers = new ArrayList<>();

        // 테스트와 무관하게 저장되어 있는 데이터 개수
        int preset = userCouponRepository.findAll().size();

        // 쿠폰 저장
        Coupon testCoupon = Coupon.builder()
                .couponType(CouponType.GENERAL)
                .couponName("test")
                .validityDays(0)
                .totalCount(couponQuantity)
                .discountType(DiscountType.FIXED_AMOUNT)
                .minPrice(BigDecimal.valueOf(50000))
                .discountPrice(BigDecimal.valueOf(5000))
                .eligibleGrade(UserGrade.NORMAL)
                .couponStatus(CouponStatus.AVAILABLE)
                .startDate(LocalDateTime.now())
                .expirationDate(LocalDateTime.now().plusMonths(1))
                .build();

        long testCouponId = couponRepository.save(testCoupon).getId();

        // 쿠폰이 데이터베이스에 저장되었는지 확인
        Optional<Coupon> savedCoupon = couponRepository.findById(testCouponId);
        assertTrue(savedCoupon.isPresent(), "쿠폰이 데이터베이스에 저장되지 않았습니다.");

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(tryCouponIssue);
        CountDownLatch latch = new CountDownLatch(tryCouponIssue);

        for (int i = 0; i < tryCouponIssue; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    System.out.println(finalI + "번째 트랜잭션 시작");
                    User testUser = User.builder()
                            .username("testUser")
                            .password("$2a$10$h/HUXk.6qYJYfPxUd/PlCuDBeR98.2DF5cqyFVzfllWpHdHlNGrGe")
                            .email("test@example.com")
                            .name("test")
                            .nickname("test")
                            .phoneNumber("01012345678")
                            .address("address")
                            .birthDay(LocalDate.now())
                            .grade(UserGrade.NORMAL)
                            .state(UserState.ACTIVE)
                            .auth(RoleType.USER)
                            .build();
                    testUsers.add(userRepository.saveAndFlush(testUser));
                    couponService.issueCoupon(testCouponId, testUser);
                    successCount.getAndIncrement();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    failCount.getAndIncrement();
                } finally {
                    System.out.println(finalI + "번째 트랜잭션 종료");
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown(); // 스레드 풀 종료

        System.out.println("success " + successCount);
        System.out.println("fail " + failCount);

        List<UserCoupon> result = userCouponRepository.findAll();

        // then
        assertEquals(couponQuantity, result.size() - preset);

        // delete test data
        for(UserCoupon issuedCouopon : result) {
            userCouponRepository.delete(issuedCouopon);
        }
        couponRepository.deleteById(testCouponId);
        for (User testUser : testUsers) {
            userRepository.delete(testUser);
        }

    }

}