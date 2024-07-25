package com.sparta.bookflex.domain.category.controller;

import com.sparta.bookflex.common.aop.Envelop;
import com.sparta.bookflex.domain.category.dto.CategoryAllResponseDto;
import com.sparta.bookflex.domain.category.dto.CategoryNameResponseDto;
import com.sparta.bookflex.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Envelop("모든 카테고리 조회에 성공하였습니다.")
    public ResponseEntity<List<CategoryAllResponseDto>> getAllCategories() {

        List<CategoryAllResponseDto> responseList = categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/main")
    @Envelop("메인 카테고리 조회에 성공하였습니다.")
    public ResponseEntity<List<CategoryNameResponseDto>> getAllMainCategories() {

        List<CategoryNameResponseDto> responseList = categoryService.getAllMainCategories();
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/sub")
    @Envelop("서브 카테고리 목록 조회에 성공하였습니다.")
    public ResponseEntity<List<CategoryNameResponseDto>> getAllSubCategories(@RequestParam(value = "mainCategory", defaultValue = "카테고리") String mainCategory) {

        List<CategoryNameResponseDto> responseList = categoryService.getAllSubCategories(mainCategory);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }
}
