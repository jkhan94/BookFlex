package com.sparta.bookflex.domain.orderbook.repository;

import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderBookQRepository {

    Page<OrderBook> findAllByPagable(Pageable pageable);
}
