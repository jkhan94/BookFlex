package com.sparta.bookflex.domain.basket.repository;

import com.sparta.bookflex.domain.basket.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    Optional<Basket> findByBookIdAndUserId(Long bookId, Long userId);

    List<Basket> findAllByUserId(Long userId);
}
