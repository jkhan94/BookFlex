package com.sparta.bookflex.domain.orderbook.repository;

import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface OrderBookQRepository {

    Page<OrderBook> findAllByPagable(Pageable pageable);
    Page<OrderBook> getOrderBooksByStatus(Pageable pageable, OrderState status, LocalDate startDate, LocalDate endDate, String username);
}
