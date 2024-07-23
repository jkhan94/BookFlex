package com.sparta.bookflex.domain.basket.repository;

import com.sparta.bookflex.domain.basket.entity.Basket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    Optional<Basket> findByBookIdAndUserId(Long bookId, Long userId);

    Page<Basket> findAllByUserId(Long userId, Pageable pageable);

    Optional<Basket> findByUserIdAndBookId(long id, Long bookId);

    boolean existsByBookIdAndUserId(Long bookId, Long userId);
}
