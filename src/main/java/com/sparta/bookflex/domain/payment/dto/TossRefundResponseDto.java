package com.sparta.bookflex.domain.payment.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TossRefundResponseDto {
    private String refundKey;    // 환불 식별 키
    private String paymentKey;   // 결제 식별 키
    private String amount;       // 환불된 금액
    private String status;       // 환불 상태 (예: "success", "failed")
    private String requestedAt;  // 환불 요청 시간
    private String approvedAt;   // 환불 승인 시간
    private String reason;       // 환불 사유 (선택적)
}