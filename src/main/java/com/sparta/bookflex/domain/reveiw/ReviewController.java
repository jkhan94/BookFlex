package com.sparta.bookflex.domain.reveiw;

import com.sparta.bookflex.common.dto.CommonDto;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.book.dto.BookResponseDto;
import com.sparta.bookflex.domain.reveiw.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/sale/{saleId}/reviews")
    public CommonDto<ReviewResponseDto> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(value = "saleId") Long saleId,
            @RequestBody ReviewRequestDto reviewRequestDto
    ) {

        ReviewResponseDto reviewResponseDto = reviewService.createReview(userDetails.getUser(), saleId, reviewRequestDto);

        return new CommonDto<>(HttpStatus.CREATED.value(), "리뷰 등록에 성공하였습니다.", reviewResponseDto);
    }

    @GetMapping("/reviews/{reviewId}")
    public CommonDto<ReviewResponseDto> getReview(@PathVariable(value = "reviewId") Long reviewId) {

        ReviewResponseDto reviewResponseDto = reviewService.getReview(reviewId);

        return new CommonDto<>(HttpStatus.CREATED.value(), "리뷰를 조회했습니다.", reviewResponseDto);
    }

    @GetMapping("/reviews")
    public CommonDto<List<ReviewResponseDto>> getAllReviews() {

        List<ReviewResponseDto> reviewResponseDto = reviewService.getAllReviews();

        return new CommonDto<>(HttpStatus.CREATED.value(), "리뷰를 조회했습니다.", reviewResponseDto);
    }

    @PutMapping("/reviews/{reviewId}")
    public CommonDto<ReviewResponseDto> modifyReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(value = "reviewId") Long reviewId,
            @RequestBody ReviewRequestDto reviewRequestDto
    ) {

        ReviewResponseDto reviewResponseDto = reviewService.modifyReview(userDetails.getUser(), reviewId, reviewRequestDto);

        return new CommonDto<>(HttpStatus.OK.value(), "리뷰 등록에 성공하였습니다.", reviewResponseDto);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public CommonDto<String> deleteReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(value = "reviewId") Long reviewId

    ) {

        String reviewTitle = reviewService.deleteReview(userDetails.getUser(), reviewId);

        return new CommonDto<>(HttpStatus.OK.value(), "리뷰 등록에 성공하였습니다.", reviewTitle + " 리뷰를 삭제하였습니다.");
    }

}
