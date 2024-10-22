package com.sparta.bookflex.domain.book.dto;

import com.sparta.bookflex.domain.book.entity.Book;
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
    private String mainCategoryName;
    private String subCategoryName;
    private String photoImagePath;
    private double avgStar;
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
                           String mainCategoryName,
                           String subCategoryName,
                           String photoImagePath,
                           double avgStar,
                           LocalDateTime createdAt,
                           LocalDateTime modifiedAt
    ) {

        this.bookId = bookId;
        this.bookName = bookName;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.stock = stock;
        this.bookDescription = bookDescription;
        this.status = status;
        this.mainCategoryName = mainCategoryName;
        this.subCategoryName = subCategoryName;
        this.photoImagePath = photoImagePath;
        this.avgStar = avgStar;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;

    }

    public BookResponseDto(Book book) {
    }
}
