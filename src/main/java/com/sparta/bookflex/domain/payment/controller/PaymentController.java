package com.sparta.bookflex.domain.payment.controller;

import com.sparta.bookflex.common.dto.CommonDto;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.payment.dto.*;
import com.sparta.bookflex.domain.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payements")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/toss")
    public ResponseEntity<CommonDto<String>> requestPayment(@RequestBody TossPaymentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String checkedUrl =  paymentService.createPayment(requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonDto<>(HttpStatus.CREATED.value(), "주문이 완료되었습니다.", checkedUrl));
    }

    @PostMapping("/success")
    public ResponseEntity<String> handlePaymentSuccess(@RequestBody TossPaymentResponseDto responseDto) {
        // 결제 성공 처리 로직
        // 예를 들어, 주문 상태 업데이트
        return ResponseEntity.ok("Payment successful");
    }

    @PostMapping("/fail")
    public ResponseEntity<String> handlePaymentFail(@RequestBody TossPaymentResponseDto responseDto) {
        // 결제 실패 처리 로직
        // 예를 들어, 주문 상태 업데이트
        return ResponseEntity.ok("Payment failed");
    }

//    @PostMapping("/refund")
//    public ResponseEntity<TossRefundResponseDto> processRefund(@RequestBody TossRefundRequestDto refundRequestDto) {
//        try {
//            TossRefundResponseDto responseDto = paymentService.processRefund(
//                    refundRequestDto.getPaymentKey(),
//                    refundRequestDto.getAmount(),
//                    refundRequestDto.getReason()
//            );
//            return ResponseEntity.ok(responseDto);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }


}
