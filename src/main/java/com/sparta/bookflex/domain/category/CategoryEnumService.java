package com.sparta.bookflex.domain.category;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryEnumService {

    public List<CategoryEnumResponseDto> getAllCategories() {
        List<CategoryEnumResponseDto> allCategories = new ArrayList<>();
        List<Category> allMain = new ArrayList<>();
        List<String> allSub = new ArrayList<>();

        for (Category c : Category.values()) {
            if (c.getMainCategory().equals(Optional.of(Category.ROOT))) {
                allMain.add(c);
            }
        }
        for (Category value : allMain) {
            for (Category c : Category.values()) {
                if (c.getMainCategory().equals(Optional.of(value))) {
                    allSub.add(c.getCategoryName());
                }
            }

            allCategories.add(CategoryEnumResponseDto.builder()
                    .mainCategoryName(value.getCategoryName())
                    .subCategoryNames(allSub)
                    .build());

            allSub = new ArrayList<>();
        }

        return allCategories;
    }

    public List<CategoryNameResponseDto> getAllMainCategories() {
        List<String> mainCategories = new ArrayList<>();

        for (Category c : Category.values()) {
            if (c.getMainCategory().equals(Optional.of(Category.ROOT))) {
                mainCategories.add(c.getCategoryName());
            }
        }
        return mainCategories.stream().map(
                category -> CategoryNameResponseDto.builder()
                        .categoryName(category)
                        .build()
        ).toList();

    }

    public List<CategoryNameResponseDto> getAllSubCategories(String mainCategory) {
        List<String> subC = new ArrayList<>();
        Category mainC = Category.findMainCategoryByName(mainCategory);

        for (Category c : Category.values()) {
            if (c.getMainCategory().equals(Optional.of(mainC))) {
                subC.add(c.getCategoryName());
            }
        }
        return subC.stream().map(
                category -> CategoryNameResponseDto.builder()
                        .categoryName(category)
                        .build()
        ).toList();
    }

}
