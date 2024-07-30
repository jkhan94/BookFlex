package com.sparta.bookflex.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TossPaymentResponseDto {
    @JsonProperty("code")
    private int code;

    @JsonProperty("checkoutPage")
    private String checkoutPage;

    @JsonProperty("payToken")
    private String payToken;


    @Builder
    public TossPaymentResponseDto(int code, String checkoutPage, String payToken) {
        this.code = code;
        this.checkoutPage = checkoutPage;
        this.payToken = payToken;
    }
}
