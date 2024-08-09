package com.sparta.bookflex.domain.sale.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class SaleListDto {

    private LocalDateTime createdAt;
    private Long saleId;
    private String username;
    private String bookName;
    private String orderState;
    private BigDecimal totalAmount;
    private int quantity;
    private BigDecimal price;

    @Builder
    public SaleListDto(LocalDateTime createdAt,
                       Long saleId,
                       String username,
                       String bookName,
                       String orderState,
                       BigDecimal totalAmount,
                       int quantity,
                       BigDecimal price) {

        this.createdAt = createdAt;
        this.saleId = saleId;
        this.username = username;
        this.bookName = bookName;
        this.orderState = orderState;
        this.totalAmount = totalAmount;
        this.quantity = quantity;
        this.price = price;

    }
}
