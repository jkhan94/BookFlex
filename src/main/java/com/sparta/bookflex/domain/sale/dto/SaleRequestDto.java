package com.sparta.bookflex.domain.sale.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaleRequestDto {
    private long bookId;
    private int quantity;
}
