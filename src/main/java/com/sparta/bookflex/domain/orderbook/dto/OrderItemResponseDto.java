package com.sparta.bookflex.domain.orderbook.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class OrderItemResponseDto {

    private Long orderItemId;
    private String bookName;
    private BigDecimal price;
    private int quantity;
    private BigDecimal total;;
    private String photoImagePath;
    private LocalDateTime createdAt;

    @Builder
    public OrderItemResponseDto(Long orderItemId, String bookName, BigDecimal price, int quantity,  LocalDateTime createdAt, BigDecimal total,String photoImagePath) {
        this.orderItemId = orderItemId;
        this.bookName = bookName;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.total = total;
        this.photoImagePath = photoImagePath;
    }



}
