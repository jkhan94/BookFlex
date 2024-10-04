package com.sparta.bookflex.domain.category.dto;

import com.sparta.bookflex.domain.category.enums.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CategoryAllResponseDto {
    private String mainCategoryName;
    private List<String> subCategoryNames;


    @Builder
    public CategoryAllResponseDto(String mainCategoryName, List<String> subCategoryNames) {
        this.mainCategoryName = mainCategoryName;
        this.subCategoryNames = subCategoryNames;
    }
}

