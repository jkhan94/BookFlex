package com.sparta.bookflex.domain.book.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BestSellerDto {
    private Long bookId;
    private String bookName;
    private Integer quantity;
    private String ImagePath;

    @Builder
    public BestSellerDto(Long bookId, String bookName, Integer quantity, String imagePath) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.quantity = quantity;
        this.ImagePath = imagePath;
    }
}
