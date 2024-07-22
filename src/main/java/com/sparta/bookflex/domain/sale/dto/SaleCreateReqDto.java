package com.sparta.bookflex.domain.sale.dto;

import lombok.Getter;

@Getter
public class SaleCreateReqDto {
    private long bookId;
    private int quantity;
}
