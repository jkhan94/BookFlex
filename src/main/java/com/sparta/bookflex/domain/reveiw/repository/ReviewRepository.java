package com.sparta.bookflex.domain.reveiw.repository;

import com.sparta.bookflex.domain.reveiw.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {


    Page<Review> findByBookId(Long bookId, Pageable pageble);

}
