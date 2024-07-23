package com.sparta.bookflex.domain.category.controller;

import com.sparta.bookflex.common.aop.Envelop;
import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.category.dto.CategoryRequestDto;
import com.sparta.bookflex.domain.category.dto.CategoryResponseDto;
import com.sparta.bookflex.domain.category.service.CategoryService;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.enums.UserRole;
import com.sparta.bookflex.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.bookflex.common.exception.ErrorCode.USER_NOT_AUTHORIZED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final AuthService authService;

    @Envelop("카테고리를 추가했습니다.")
    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @RequestBody @Valid CategoryRequestDto requestDto) {

        checkUser(userDetails);

        CategoryResponseDto responseDto = categoryService.createCategory(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{categoryId}")
    @Envelop("카테고리 조회에 성공하였습니다.")
    public ResponseEntity<CategoryResponseDto> getSingleCategory(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @PathVariable long categoryId,
                                                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                                                 @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy) {

        CategoryResponseDto responseDto = categoryService.getSingleCategory(categoryId, page - 1, sortBy);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping
    @Envelop("카테고리 목록 조회에 성공하였습니다.")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                                                      @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy) {

        List<CategoryResponseDto> responseList = categoryService.getAllCategories(page - 1, sortBy);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @PutMapping("/{categoryId}")
    @Envelop("카테고리를 수정했습니다.")
    public ResponseEntity<CategoryResponseDto> updateCategory(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @PathVariable long categoryId,
                                                              @RequestBody @Valid CategoryRequestDto requestDto) {

        checkUser(userDetails);

        CategoryResponseDto responseDto = categoryService.updateCategory(categoryId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @DeleteMapping("/{categoryId}")
    @Envelop("카테고리를 삭제했습니다.")
    public ResponseEntity deleteCategory(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @PathVariable long categoryId) {

        checkUser(userDetails);

        categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private void checkUser(UserDetailsImpl userDetails) {
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != UserRole.ADMIN) {
            throw new BusinessException(USER_NOT_AUTHORIZED);
        }
    }
}
