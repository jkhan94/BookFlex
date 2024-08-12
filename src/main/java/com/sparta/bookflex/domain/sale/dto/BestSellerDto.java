package com.sparta.bookflex.domain.sale.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BestSellerDto {
    private String bookName;
    private String ImagePath;

    @Builder
    public BestSellerDto(String bookName, String imagePath) {
        this.bookName = bookName;
        ImagePath = imagePath;
    }
}
