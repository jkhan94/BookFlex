package com.sparta.bookflex.domain.book.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class BookResponseDto {
    private Long bookId;
    private String bookName;
    private String publisher;
    private String author;
    private BigDecimal price;
    private int stock;
    private String bookDescription;
    private String status;
    private String categoryName;
    private int discountRate;
    private String photoImagePath;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    @Builder
    public BookResponseDto(Long bookId,
                           String bookName,
                           String author,
                           String publisher,
                           BigDecimal price,
                           int stock,
                           String bookDescription,
                           String status,
                           String categoryName,
                           String photoImagePath,
                           LocalDateTime createdAt,
                           LocalDateTime modifiedAt,
                           int discountRate) {

        this.bookId = bookId;
        this.bookName = bookName;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.stock = stock;
        this.bookDescription = bookDescription;
        this.status = status;
        this.categoryName = categoryName;
        this.photoImagePath = photoImagePath;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.discountRate = discountRate;
    }
}
