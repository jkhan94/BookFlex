package com.sparta.bookflex.domain.orderbook.repository;

import com.sparta.bookflex.domain.orderbook.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
