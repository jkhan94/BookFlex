package com.sparta.bookflex.domain.orderbook.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderCreateResponseDto {

    private Long orderId;

    @Builder
    public OrderCreateResponseDto(Long orderId) {
        this.orderId = orderId;
    }
}
