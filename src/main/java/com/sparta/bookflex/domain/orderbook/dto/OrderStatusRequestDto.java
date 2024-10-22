package com.sparta.bookflex.domain.orderbook.dto;

import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderStatusRequestDto {
    private OrderState status;

    @Builder
    public OrderStatusRequestDto(OrderState status) {
        this.status = status;
    }
}
