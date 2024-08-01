package com.sparta.bookflex.domain.reveiw.repository;

import com.sparta.bookflex.domain.reveiw.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewCustomRepository {

    Page<Review> getReviewsByBookName(String bookName, Pageable pageble);
}
