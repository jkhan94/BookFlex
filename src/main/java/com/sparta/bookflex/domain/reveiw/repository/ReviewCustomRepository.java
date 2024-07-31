package com.sparta.bookflex.domain.reveiw.repository;

import com.sparta.bookflex.domain.reveiw.entity.Review;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewCustomRepository {

    Page<Review> getReview(String bookName, Pageable pageable);
}
