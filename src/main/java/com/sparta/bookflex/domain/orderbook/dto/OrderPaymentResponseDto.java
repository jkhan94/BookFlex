package com.sparta.bookflex.domain.orderbook.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderPaymentResponseDto {
    private Long orderId;
    private String orderNo;
    private String orderName;

    private String customerEmail;
    private String customerName;
    private String customerMobilePhone;
    private int paymentAmount;

    @Builder
    public OrderPaymentResponseDto(Long orderId, String orderName, String customerEmail, String customerName, String customerMobilePhone,String orderNo,int paymentAmount) {
        this.orderId = orderId;
        this.orderNo = orderNo;;
        this.orderName = orderName;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.customerMobilePhone = customerMobilePhone;
        this.paymentAmount = paymentAmount;
    }

}
