package com.sparta.bookflex.domain.orderbook.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bookflex.domain.book.entity.BookStatus;
import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.orderbook.entity.QOrderBook;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.sparta.bookflex.domain.book.entity.QBook.book;
import static com.sparta.bookflex.domain.orderbook.entity.QOrderBook.orderBook;
import static com.sparta.bookflex.domain.sale.entity.QSale.sale;
import static com.sparta.bookflex.domain.user.entity.QUser.user;

@RequiredArgsConstructor
@Repository
public class OrderBookQRepositoryImpl implements OrderBookQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrderBook> findAllByPagable(Pageable pageable) {

        QOrderBook qOrderBook = orderBook;
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

    @Override
    public Page<OrderBook> getOrderBooksByStatus(Pageable pageable, OrderState status, LocalDate startDate, LocalDate endDate, String username) {

        List<OrderBook> orderBookList = queryFactory
                .select(orderBook)
                .from(orderBook)
                .where(eqUsername(username))
                .where(eqStatus(status))
                .where(searchDateFilter(startDate, endDate))
                .orderBy(orderBook.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(orderBook.count())
                .from(orderBook)
                .fetchOne();

        long size = (count != null) ? count : 0L;

        return new PageImpl<>(orderBookList, pageable, size);
    }

    private BooleanExpression eqStatus(OrderState status) {

        if (StringUtils.isEmpty(status)) {
            return null;
        }

        return orderBook.status.eq(status);
    }

    private BooleanExpression eqUsername(String username) {

        if (StringUtils.isEmpty(username)) {
            return null;
        }

        return orderBook.user.username.eq(username);
    }

    private BooleanExpression searchDateFilter(LocalDate searchStartDate, LocalDate searchEndDate) {

        BooleanExpression isGoeStartDate = orderBook.createdAt.goe(LocalDateTime.of(searchStartDate, LocalTime.MIN));
        BooleanExpression isLoeEndDate = orderBook.createdAt.loe(LocalDateTime.of(searchEndDate, LocalTime.MAX).withNano(0));

        return Expressions.allOf(isGoeStartDate, isLoeEndDate);
    }
}
