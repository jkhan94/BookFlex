package com.sparta.bookflex.domain.basket.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class BasketResDto {

    private Long basketId;
    //private PhotoImage photoImage;
    private String bookName;
    private BigDecimal price;
    private int quantity;

    @Builder
    public BasketResDto(Long baseketId, String bookName, BigDecimal price, int quantity) {
        this.basketId = baseketId;
        //this.photoImage = photoImage;
        this.bookName = bookName;
        this.price = price;
        this.quantity = quantity;
    }
}
