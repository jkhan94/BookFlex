package com.sparta.bookflex.domain.basket.repository;

import com.sparta.bookflex.domain.basket.entity.Basket;
import com.sparta.bookflex.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {
}
