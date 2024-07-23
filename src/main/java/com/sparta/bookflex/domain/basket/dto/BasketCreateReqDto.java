package com.sparta.bookflex.domain.basket.dto;

import lombok.Getter;

@Getter
public class BasketCreateReqDto {
    private Long bookId;
    private int quantity;
}
