package com.sparta.bookflex.domain.payment.dto;

import lombok.Getter;

@Getter
public class SuccessPayReqDto {
    String orderId;
    int amount;
    String paymentKey;
}
