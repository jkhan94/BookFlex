package com.sparta.bookflex.domain.orderbook.dto;

import com.sparta.bookflex.domain.sale.dto.SaleResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResponsDto {
    private Long orderId;
    private LocalDateTime createdAt;
    private int total;
    private String status;
    private List<SaleResponseDto> sales;

    @Builder
    public OrderResponsDto(Long orderId, LocalDateTime createdAt, int total, String status, List<SaleResponseDto> sales) {
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.total = total;
        this.status = status;
    }

}
