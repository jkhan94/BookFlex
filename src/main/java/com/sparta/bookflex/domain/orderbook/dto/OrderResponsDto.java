package com.sparta.bookflex.domain.orderbook.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResponsDto {
    private Long orderId;
    private String orderNo;
    private LocalDateTime createdAt;
    private BigDecimal total;
    private String status;
    private List<OrderItemResponseDto> orderItemResponseDtoList;

    @Builder
    public OrderResponsDto(Long orderId, LocalDateTime createdAt, BigDecimal total, String status, List<OrderItemResponseDto> orderItemResponseDtoList, String orderNo) {
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.total = total;
        this.status = status;
        this.orderItemResponseDtoList = orderItemResponseDtoList;
        this.orderNo = orderNo;
    }

}
