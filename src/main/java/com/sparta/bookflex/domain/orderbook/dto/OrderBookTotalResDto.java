package com.sparta.bookflex.domain.orderbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class OrderBookTotalResDto {
    long totalCount;
    Page<OrderShipResDto> orderShipResDtos;

    public OrderBookTotalResDto(long totalCount, Page<OrderShipResDto> orderShipResDtos) {
        this.totalCount = totalCount;
        this.orderShipResDtos = orderShipResDtos;
    }
}
