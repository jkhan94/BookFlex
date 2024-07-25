package com.sparta.bookflex.domain.category.service;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.domain.category.dto.CategoryAllResponseDto;
import com.sparta.bookflex.domain.category.dto.CategoryNameResponseDto;
import com.sparta.bookflex.domain.category.enums.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sparta.bookflex.common.exception.ErrorCode.NOT_LEAF_CATEGORY;
import static com.sparta.bookflex.domain.category.enums.Category.findCategoryByName;

@Service
public class CategoryService {

    public List<CategoryAllResponseDto> getAllCategories() {
        List<CategoryAllResponseDto> allCategories = new ArrayList<>();
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

            allCategories.add(CategoryAllResponseDto.builder()
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
        Category mainC = findCategoryByName(mainCategory);

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

    public Category getCategoryByCategoryName(String categoryName) {
        Category category = findCategoryByName(categoryName);

        if (!category.isLeafCategory()) {
            throw new BusinessException(NOT_LEAF_CATEGORY);
        }

        return category;
    }
}
