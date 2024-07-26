package com.sparta.bookflex.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TossPaymentStatusDto {
    private String paymentKey;
    private String orderId;
    private String status; // 상태 (예: "PAID", "FAILED")
    private String requestedAt;
    private String approvedAt;
}