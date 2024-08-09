package com.sparta.bookflex.domain.reveiw.cotroller;

import com.sparta.bookflex.common.dto.CommonDto;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.reveiw.dto.ReviewRequestDto;
import com.sparta.bookflex.domain.reveiw.dto.ReviewResponseDto;
import com.sparta.bookflex.domain.reveiw.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/sale/{itemId}/reviews")
    public CommonDto<ReviewResponseDto> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(value = "itemId") Long itemId,
            @RequestBody ReviewRequestDto reviewRequestDto
    ) {

        ReviewResponseDto reviewResponseDto = reviewService.createReview(userDetails.getUser(), itemId, reviewRequestDto);

        return new CommonDto<>(HttpStatus.CREATED.value(), "리뷰 등록에 성공하였습니다.", reviewResponseDto);
    }

    @GetMapping("/reviews/{reviewId}")
    public CommonDto<ReviewResponseDto> getReview(@PathVariable(value = "reviewId") Long reviewId) {

        ReviewResponseDto reviewResponseDto = reviewService.getReview(reviewId);

        return new CommonDto<>(HttpStatus.CREATED.value(), "리뷰를 조회했습니다.", reviewResponseDto);
    }

    @GetMapping("/reviews/book/{bookId}")
    public CommonDto<Page<ReviewResponseDto>> getReviewByBookId(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                                @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                                @RequestParam(name = "direction", required = false, defaultValue = "true") boolean isAsc,
                                                                @RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
                                                                @PathVariable(value = "bookId", required = false) Long bookId) {

        Page<ReviewResponseDto> reviewResponseDto = reviewService.getReviewByBookId(page, size, isAsc, sortBy, bookId);

        return new CommonDto<>(HttpStatus.CREATED.value(), "리뷰를 조회했습니다.", reviewResponseDto);
    }

    @GetMapping("/reviews")
    public CommonDto<Page<ReviewResponseDto>> getAllReviews(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                            @RequestParam(name = "direction", required = false, defaultValue = "true") boolean isAsc,
                                                            @RequestParam(name = "sortBy", required = false, defaultValue = "sortBy") String sortBy,
                                                            @RequestParam(name = "bookName", required = false) String bookName) {

        Page<ReviewResponseDto> reviewResponseDto = reviewService.getAllReviews(page, size, isAsc, sortBy, bookName);

        return new CommonDto<>(HttpStatus.CREATED.value(), "리뷰를 조회했습니다.", reviewResponseDto);
    }

    @PutMapping("/reviews/{reviewId}")
    public CommonDto<ReviewResponseDto> modifyReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(value = "reviewId") Long reviewId,
            @RequestBody ReviewRequestDto reviewRequestDto
    ) {

        ReviewResponseDto reviewResponseDto = reviewService.modifyReview(userDetails.getUser(), reviewId, reviewRequestDto);

        return new CommonDto<>(HttpStatus.OK.value(), "리뷰 수정에 성공하였습니다.", reviewResponseDto);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public CommonDto<String> deleteReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(value = "reviewId") Long reviewId

    ) {

        String reviewTitle = reviewService.deleteReview(userDetails.getUser(), reviewId);

        return new CommonDto<>(HttpStatus.OK.value(), "리뷰 삭제에 성공하였습니다.", reviewTitle + " 리뷰를 삭제하였습니다.");
    }

}
