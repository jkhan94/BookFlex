package com.sparta.bookflex.domain.payment.dto;

import lombok.Getter;

@Getter
public class FailPayReqDto {
    String orderId;
    String errorMessage;
}
