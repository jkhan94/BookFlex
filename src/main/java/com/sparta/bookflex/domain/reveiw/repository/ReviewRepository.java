package com.sparta.bookflex.domain.reveiw.repository;

import com.sparta.bookflex.domain.reveiw.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
