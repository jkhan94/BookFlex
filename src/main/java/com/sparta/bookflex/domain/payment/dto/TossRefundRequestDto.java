package com.sparta.bookflex.domain.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TossRefundRequestDto {
    private String apiKey;                // 상점의 API Key(필수)
    private String payToken;             // 결제 고유 번호(필수)
    private int amount;                 // 환불할 금액(필수)
    private int amountTaxable;          // 환불할 금액 중 과세 금액(필수)
    private int amountTaxFree;          // 환불할 금액 중 비과세 금액
    private int amountVat;              // 환불할 금액 중 부가세
    private int amountServiceFee;       // 환불할 금액 중 봉사료
    private String reason;               // 환불할 내용
}
