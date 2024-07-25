package com.sparta.bookflex.domain.basket.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class BasketResDto {

    private Long bookId;
    private String bookName;
    private BigDecimal price;
    private int quantity;
    private String photoImagePath;

    @Builder
    public BasketResDto(Long bookId, String bookName, BigDecimal price, int quantity,String photoImagePath) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.price = price;
        this.quantity = quantity;
        this.photoImagePath = photoImagePath;
    }
}
