package com.sparta.bookflex.domain.payment.dto;


import com.sparta.bookflex.domain.payment.enums.PayType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TossPaymentRequestDto {

   // private String orderNo;
    private Long orderId;                      // 주문 번호(필수)
    private int amount;                        // 결제 금액(필수)
    private int amountTaxFree;                 // 비과세 금액(필수)
    private String productDesc;                // 상품 설명(필수)
    private String apiKey;                     // API Key(testKey)
    private String retUrl;                     // 결제 완료 후 리다이렉트 URL(필수)
    private String retCancelUrl;               // 결제 취소 시 리다이렉트 URL(필수)
    private boolean autoExecute;
    private String resultCallback;             // 결제 결과 콜백 URL
    private String callbackVersion;            // 콜백 버전
    private int amountTaxable;                 // 과세 금액
    private int amountVat;                     // 부가세 금액
    private int amountServiceFee;              // 서비스 수수료
    private LocalDateTime expiredTime;         // 결제 만료 시각
    private boolean cashReceipt;
    private PayType payType;                   // 현금 영수증 발급 여부


}