package com.sparta.bookflex.domain.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryNameResponseDto {
    private String categoryName;

    @Builder
    public CategoryNameResponseDto(String categoryName) {
        this.categoryName = categoryName;
    }
}

