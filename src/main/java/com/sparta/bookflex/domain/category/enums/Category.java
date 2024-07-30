package com.sparta.bookflex.domain.category.enums;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.exception.ErrorCode;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

import static com.sparta.bookflex.common.exception.ErrorCode.CATEGORY_NOT_FOUND;

public enum Category {
    ROOT("카테고리", null),
    DOMESTIC("국내도서", ROOT),
    FICTION("소설", DOMESTIC),
    SOCIAL_SCIENCE("인문", DOMESTIC),
    BUSINESS("경제", DOMESTIC),
    HISTORY("역사", DOMESTIC),
    RELIGION("종교", DOMESTIC),
    ART("예술", DOMESTIC),
    SCIENCE("과학", DOMESTIC),
    SELF_HELP("자기계발", DOMESTIC),
    TRAVEL("여행", DOMESTIC),
    COOKING("요리", DOMESTIC),
    PARENTING("육아", DOMESTIC),
    MAGAZINE("잡지", DOMESTIC),
    DICTIONARY("사전", DOMESTIC),
    FOREIGN_LANGUAGES("외국어", DOMESTIC),
    EXAMS("참고서", DOMESTIC),

    INTERNATIONAL("외국도서", ROOT),
    ENGLISH("영미도서", INTERNATIONAL),
    JAPAN("일본도서", INTERNATIONAL),
    CHINA("중국도서", INTERNATIONAL),
    FRANCE("프랑스도서", INTERNATIONAL),
    GERMANY("독일도서", INTERNATIONAL),
    SPAIN("스페인도서", INTERNATIONAL);

    // 카테고리 이름
    @Getter
    private final String categoryName;

    // 부모 카테고리
    private final Category mainCategory;

    // 자식카테고리
    private final List<Category> subCategories;

    Category(String categoryName, Category mainCategory) {
        this.subCategories = new ArrayList<>();
        this.categoryName = categoryName;
        this.mainCategory = mainCategory;
        if (Objects.nonNull(mainCategory)) {
            mainCategory.subCategories.add(this);
        }
    }

    // 부모카테고리 Getter
    public Optional<Category> getMainCategory() {
        return Optional.ofNullable(mainCategory);
    }

    // 자식카테고리 Getter
    public List<Category> getSubCategories() {
        return Collections.unmodifiableList(subCategories);
    }

    // 메인 카테고리 이름으로 enum 찾기
    public static Category findCategoryByName(String categoryName) {
        for (Category c : Category.values()) {
            if (c.getCategoryName().equals(categoryName)) {
                return c;
            }
        }
        throw new BusinessException(CATEGORY_NOT_FOUND);
    }

    // 마지막 카테고리(상품추가 가능)들 반환
    public List<Category> getLeafCategories() {
        return Arrays.stream(Category.values())
                .filter(category -> category.isLeafCategoryOf(this))
                .collect(Collectors.toList());
    }

    private boolean isLeafCategoryOf(Category category) {
        return this.isLeafCategory() && category.contains(this);
    }

    // 마지막 카테고리(상품추가 가능)인지 반환
    public boolean isLeafCategory() {
        return subCategories.isEmpty();
    }


    private boolean contains(Category category) {
        if (this.equals(category)) return true;

        return Objects.nonNull(category.mainCategory) &&
                this.contains(category.mainCategory);
    }

    public static Category of(String Category) {
        return Arrays.stream(values())
                .filter(C -> Category.equals(C.categoryName))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
