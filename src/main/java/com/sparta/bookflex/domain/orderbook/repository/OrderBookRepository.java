package com.sparta.bookflex.domain.orderbook.repository;

import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderBookRepository extends JpaRepository<OrderBook, Long> {
    Optional<OrderBook> findByIdAndUser(Long orderId, User user);

    OrderBook findByOrderNo(String orderNo);
}
