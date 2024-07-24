package com.sparta.bookflex.domain.orderbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderRequestDto {
    private List<OrderItemDto> items;

    @Getter
    @NoArgsConstructor
    public static class OrderItemDto {
        private Long bookId;
        private int quantity;
    }
}
