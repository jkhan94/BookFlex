package com.sparta.bookflex.domain.category.service;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.domain.book.dto.BookResponseDto;
import com.sparta.bookflex.domain.category.dto.CategoryRequestDto;
import com.sparta.bookflex.domain.category.dto.CategoryResponseDto;
import com.sparta.bookflex.domain.category.entity.Category;
import com.sparta.bookflex.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.sparta.bookflex.common.exception.ErrorCode.CATEGORY_ALREADY_EXISTS;
import static com.sparta.bookflex.common.exception.ErrorCode.CATEGORY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private static final int PAGE_SIZE = 5;

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {

        checkCategoryName(requestDto);

        Category category = Category.builder()
                .categoryName(requestDto.getCategoryName())
                .build();

        categoryRepository.save(category);

        return CategoryResponseDto.builder()
                .categoryName(category.getCategoryName())
                .createdAt(category.getCreatedAt())
                .modifiedAt(category.getModifiedAt())
                .bookList(new ArrayList<>())
                .build();
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto getSingleCategory(long categoryId, int page, String sortBy) {
        Category category = getCategoryById(categoryId);

        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

        Page<BookResponseDto> bookPage = categoryRepository.findAllBooks(categoryId, pageable).map(
                book -> BookResponseDto.builder()
                        .bookId((book.getId()))
                        .bookName(book.getBookName())
                        .author(book.getAuthor())
                        .publisher(book.getPublisher())
                        .price(book.getPrice())
                        .stock(book.getStock())
                        .bookDescription(book.getBookDescription())
                        .status(book.getStatus())
                        .categoryName(book.getCategory().getCategoryName())
                        .photoImagePath(book.getPhotoImage().getFilePath())
                        .createdAt(book.getCreatedAt())
                        .modifiedAt(book.getModifiedAt())
                        .build()
        );
        List<BookResponseDto> bookList = bookPage.getContent();

        return CategoryResponseDto.builder()
                .categoryName(category.getCategoryName())
                .createdAt(category.getCreatedAt())
                .modifiedAt(category.getModifiedAt())
                .bookList(bookList)
                .build();
    }

    @Transactional//(readOnly = true)
    public List<CategoryResponseDto> getAllCategories(int page, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

        Page<CategoryResponseDto> categoryPage = categoryRepository.findAll(pageable).map(
                category -> CategoryResponseDto.builder()
                        .categoryName(category.getCategoryName())
                        .createdAt(category.getCreatedAt())
                        .modifiedAt(category.getModifiedAt())
                        .bookList(new ArrayList<>())
                        .build()
        );
        List<CategoryResponseDto> categoryList = categoryPage.getContent();
        return categoryList;
    }

    @Transactional
    public CategoryResponseDto updateCategory(long categoryId, CategoryRequestDto requestDto) {
        Category category = getCategoryById(categoryId);

        checkCategoryName(requestDto);

        category.update(requestDto);
        categoryRepository.save(category);

        return CategoryResponseDto.builder()
                .categoryName(category.getCategoryName())
                .createdAt(category.getCreatedAt())
                .modifiedAt(category.getModifiedAt())
                .build();
    }

    @Transactional
    public void deleteCategory(long categoryId) {
        Category category = getCategoryById(categoryId);

        categoryRepository.delete(category);
    }

    private Category getCategoryById(long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new BusinessException(CATEGORY_NOT_FOUND)
        );
    }

    private void checkCategoryName(CategoryRequestDto requestDto) {
        if (categoryRepository.findByCategoryName(requestDto.getCategoryName()).isPresent()) {
            throw new BusinessException(CATEGORY_ALREADY_EXISTS);
        }
    }

}
