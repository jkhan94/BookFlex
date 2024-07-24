package com.sparta.bookflex.domain.category;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CategoryEnumResponseDto {
    private String mainCategoryName;
    private List<String> subCategoryNames;

    @Builder
    public CategoryEnumResponseDto(String mainCategoryName, List<String> subCategoryNames) {
        this.mainCategoryName = mainCategoryName;
        this.subCategoryNames = subCategoryNames;
    }
}

