package com.sparta.bookflex.domain.orderbook.repository;

import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderBookRepository extends JpaRepository<OrderBook, Long>, OrderBookQRepository {
    Optional<OrderBook> findByIdAndUser(Long orderId, User user);

    OrderBook findByOrderNo(String orderNo);

    Page<OrderBook> findByUser(User user, Pageable pageable);


    @Query("SELECT COUNT(o.id) FROM OrderBook o")
    Long findTotalCount();
}