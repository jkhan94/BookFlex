package com.sparta.bookflex.domain.orderbook.repository;

import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderBookRepository extends JpaRepository<OrderBook, Long> {
}
