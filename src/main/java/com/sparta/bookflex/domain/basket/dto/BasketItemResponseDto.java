package com.sparta.bookflex.domain.basket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class BasketItemResponseDto {
    private Long basketItemId;
    private String bookName;
    private BigDecimal price;
    private int quantity;
    private String photoImagePath;

    @Builder
    public BasketItemResponseDto(Long basketItemId, String bookName, BigDecimal price, int quantity, String photoImagePath) {
        this.basketItemId = basketItemId;
        this.bookName = bookName;
        this.price = price;
        this.quantity = quantity;
        this.photoImagePath = photoImagePath;
    }
}
