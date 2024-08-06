package com.sparta.bookflex.domain.coupon.service;

import com.sparta.bookflex.domain.coupon.entity.Coupon;
import com.sparta.bookflex.domain.coupon.entity.UserCoupon;
import com.sparta.bookflex.domain.coupon.enums.CouponStatus;
import com.sparta.bookflex.domain.coupon.enums.CouponType;
import com.sparta.bookflex.domain.coupon.enums.DiscountType;
import com.sparta.bookflex.domain.coupon.repository.CouponRepository;
import com.sparta.bookflex.domain.coupon.repository.UserCouponRepository;
import com.sparta.bookflex.domain.qna.dto.ReplyRequestDto;
import com.sparta.bookflex.domain.qna.entity.Qna;
import com.sparta.bookflex.domain.qna.enums.QnaTypeCode;
import com.sparta.bookflex.domain.qna.repository.QnaRepository;
import com.sparta.bookflex.domain.qna.service.QnaService;
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
public class QnaServiceTest {

    @Autowired
    private QnaService qnaService;

    @Autowired
    private QnaRepository qnaRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String WAITING_FOR_REPLY = "답변대기";

    @Test
    @DisplayName("쿠폰 발급 동시성 문제")
    void issueCouponConcurrently() throws InterruptedException {
        // given
        int leftReply = 1;
        int trySaveReply = 100; // 시도할 횟수
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // 테스트와 무관하게 저장되어 있는 데이터 개수
        int preset = qnaRepository.findAll().size();

        // 문의 남긴 고객 생성
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
        userRepository.saveAndFlush(testUser);

        // 문의 저장
        Qna testQna = Qna.builder()
                .email("test@example.com")
                .qnaType(QnaTypeCode.PRODUCT)
                .inquiry("inquiry")
                .reply(WAITING_FOR_REPLY)
                .user(testUser)
                .build();

        long testQnaId = qnaRepository.save(testQna).getId();

        // 문의가 데이터베이스에 저장되었는지 확인
        Optional<Qna> savedQna = qnaRepository.findById(testQnaId);
        assertTrue(savedQna.isPresent(), "쿠폰이 데이터베이스에 저장되지 않았습니다.");

        ReplyRequestDto testReply = new ReplyRequestDto("Test Reply");

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(trySaveReply);
        CountDownLatch latch = new CountDownLatch(trySaveReply);

        for (int i = 0; i < trySaveReply; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    System.out.println(finalI + "번째 트랜잭션 시작");
                    qnaService.createQnaReply(testReply, testQnaId);
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

        List<Qna> result = qnaRepository.findAll();

        // then
        assertEquals(leftReply, result.size() - preset);

        // delete test data
        qnaRepository.delete(testQna);
        userRepository.delete(testUser);


    }

}