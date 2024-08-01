package com.sparta.bookflex.domain.orderbook.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderCreateResponseDto {

    private Long orderId;
    private String orderNo;

    @Builder
    public OrderCreateResponseDto(Long orderId, String orderNo) {
        this.orderNo = orderNo;
        this.orderId = orderId;
    }
}
