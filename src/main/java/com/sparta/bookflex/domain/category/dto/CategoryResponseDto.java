package com.sparta.bookflex.domain.category.dto;

import com.sparta.bookflex.domain.book.dto.BookResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class CategoryResponseDto {
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<BookResponseDto> bookList;

    @Builder
    public CategoryResponseDto(String categoryName, LocalDateTime createdAt, LocalDateTime modifiedAt, List<BookResponseDto> bookList) {
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.bookList = bookList;
    }
}
