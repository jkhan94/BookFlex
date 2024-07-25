package com.sparta.bookflex.domain.basket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class BasketItemResponseDto {
    private Long baskeItemid;
    private Long bookId;
    private String bookName;
    private BigDecimal price;
    private int quantity;
    private String photoImagePath;

    @Builder
    public BasketItemResponseDto(Long baskeItemid, Long bookId, String bookName, BigDecimal price, int quantity, String photoImagePath) {
        this.baskeItemid = baskeItemid;
        this.bookId = bookId;
        this.bookName = bookName;
        this.price = price;
        this.quantity = quantity;
        this.photoImagePath = photoImagePath;
    }
}
