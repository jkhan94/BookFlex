package com.sparta.bookflex.domain.reveiw.service;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.exception.ErrorCode;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.service.BookService;
import com.sparta.bookflex.domain.orderbook.entity.OrderItem;
import com.sparta.bookflex.domain.orderbook.repository.OrderItemRepository;
import com.sparta.bookflex.domain.reveiw.dto.ReviewRequestDto;
import com.sparta.bookflex.domain.reveiw.dto.ReviewResponseDto;
import com.sparta.bookflex.domain.reveiw.entity.Review;
import com.sparta.bookflex.domain.reveiw.repository.ReviewCustomRepositoryImpl;
import com.sparta.bookflex.domain.reveiw.repository.ReviewRepository;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.sale.repository.SaleRepository;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.enums.RoleType;
import com.sparta.bookflex.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AuthService authService;
    private final BookService bookService;
//    private final SaleService saleService;
    private final SaleRepository saleRepository;
    private final ReviewCustomRepositoryImpl reviewCustomRepositoryImpl;
    private final OrderItemRepository orderItemRepository;

    /*
    레포지토리 접근 부분 추후 수정 필요
     */
    @Transactional
    public ReviewResponseDto createReview(User user, Long itemId, ReviewRequestDto reviewRequestDto) {
        User selectedUser = getUser(user.getUsername());
//        Sale selectedSale = saleService.getSale(saleId);
        OrderItem selectedOrderItem = orderItemRepository.findById(itemId).orElseThrow(() -> new BusinessException(ErrorCode.ORDER_ITEM_NOT_FOUND));
        Book selectedBook = bookService.getBookByBookId(selectedOrderItem.getBook().getId());
        selectedOrderItem.updateIsReviewed(true);
        orderItemRepository.save(selectedOrderItem);
        Review createdReview = reviewRequestDto.toEntity(selectedUser, selectedBook);

        createdReview = reviewRepository.save(createdReview);

        selectedUser.getReviewList().add(createdReview);

        selectedBook.getReviewList().add(createdReview);

        return createdReview.toResponseDto();

    }

    public ReviewResponseDto getReview(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

        return review.toResponseDto();
    }

    @Transactional
    public ReviewResponseDto modifyReview(User user, Long reviewId, ReviewRequestDto reviewRequestDto) {

        Review review = getReviewById(reviewId);

        if (user.getId() != (review.getUser().getId())) {
            throw new BusinessException(ErrorCode.USER_NOT_WRITER);
        }

        review.update(reviewRequestDto);

        return review.toResponseDto();
    }

    public String deleteReview(User user, Long reviewId) {

        Review review = getReviewById(reviewId);

        String reviewTitle = review.getTitle();

        if (user.getId() != (review.getUser().getId()) && !user.getAuth().equals(RoleType.ADMIN)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        reviewRepository.delete(review);

        return reviewTitle;
    }

    private User getUser(String username) {

        return authService.findByUserName(username);
    }

    private Review getReviewById(Long reviewId) {

        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
    }

    public List<ReviewResponseDto> getAllReviews() {

        return reviewRepository.findAll().stream().map(Review::toResponseDto).toList();
    }


    public Page<ReviewResponseDto> getReviewByBookId(int page, int size, boolean isAsc, String sortBy, Long bookId) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortBy);

        Pageable pageble = PageRequest.of(page - 1, size, sort);

//        Page<Review> reviewList = reviewCustomRepositoryImpl.findByBookId(bookName, bookId, pageble);

//        return reviewList.map(Review::toResponseDto);

        Page<Review> reviewList = reviewRepository.findByBookId(bookId, pageble);

        return reviewList.map(Review::toResponseDto);

    }

    public Page<ReviewResponseDto> getAllReviews(int page, int size, boolean isAsc, String sortBy, String bookName) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortBy);

        Pageable pageble = PageRequest.of(page - 1, size, sort);

        Page<Review> reviewList = reviewCustomRepositoryImpl.getReviewsByBookName(bookName, pageble);

        return reviewList.map(Review::toResponseDto);
    }
}
