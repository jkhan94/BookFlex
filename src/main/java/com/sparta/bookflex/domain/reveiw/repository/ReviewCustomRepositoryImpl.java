package com.sparta.bookflex.domain.reveiw.repository;

import com.sparta.bookflex.domain.reveiw.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {


    @Override
    public Page<Review> getReview(String bookName, Pageable pageable) {
        return null;
    }
}
