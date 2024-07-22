package com.sparta.bookflex.domain.book.dto;

import com.sparta.bookflex.domain.photoimage.entity.PhotoImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BookResponseDto {
    private Long bookId;
    private String bookName;
    private String author;
    private String publisher;
    private int price;
    private int stock;
    private String bookDescription;
    private String status;
    private String categoryName;
    private String photoImagePath;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public BookResponseDto(Long bookId, String bookName, String author, String publisher, int price, int stock, String bookDescription, String status, String categoryName, String photoImagePath, LocalDateTime createdAt, LocalDateTime modifiedAt) {
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
    }
}
