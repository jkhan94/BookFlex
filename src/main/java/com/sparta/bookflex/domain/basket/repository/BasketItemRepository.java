package com.sparta.bookflex.domain.basket.repository;

import com.sparta.bookflex.domain.basket.entity.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {
}
