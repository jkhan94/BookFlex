package com.sparta.bookflex.domain.orderbook.dto;

import lombok.Getter;

@Getter
public class OrderPaymentRequestDto {
    Long orderId;
    Long userCouponId;
}
