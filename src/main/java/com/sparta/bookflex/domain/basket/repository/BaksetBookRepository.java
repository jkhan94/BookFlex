package com.sparta.bookflex.domain.basket.repository;

import com.sparta.bookflex.domain.basket.entity.BasketBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaksetBookRepository extends JpaRepository<BasketBook, Long> {
}
