package com.sparta.bookflex.domain.basket.repository;

import com.sparta.bookflex.domain.basket.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {
}
