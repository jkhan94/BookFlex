package com.sparta.bookflex.domain.orderbook.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.orderbook.entity.QOrderBook;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class OrderBookQRepositoryImpl implements OrderBookQRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrderBook> findAllByPagable(Pageable pageable) {

        QOrderBook qOrderBook = QOrderBook.orderBook;
        List<OrderBook> orderBookList = queryFactory
            .select(qOrderBook)
            .from(qOrderBook)
            .orderBy(qOrderBook.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = queryFactory
            .select(qOrderBook.count())
            .from(qOrderBook)
            .fetchOne();

        long size = (count != null) ? count : 0L;

        return new PageImpl<>(orderBookList, pageable, size);
    }
}
