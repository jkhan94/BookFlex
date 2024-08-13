package com.sparta.bookflex.domain.book.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.entity.BookStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.sparta.bookflex.domain.book.entity.QBook.book;
import static com.sparta.bookflex.domain.sale.entity.QSale.sale;

@Repository
@RequiredArgsConstructor
public class BookCustomRepositoryImpl implements BookCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Book> findBooks(String bookName, BookStatus bookStatus, Pageable pageable) {
        List<Book> result = queryFactory.select(book)
                .from(book)
                .where(eqStatus(bookStatus))
                .where(eqBookName(bookName))
                .orderBy(bookSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(eqStatus(bookStatus))
                .orderBy(bookSort(pageable))
                .fetchOne();

        return new PageImpl<>(result, pageable, count);

    }

    @Override
    public List<Tuple> findBestSeller(LocalDateTime currentDateTime) {
        List<Tuple> result = queryFactory
                .select(sale.book.id, sale.book.bookName, sale.quantity.sum(), book.photoImage.filePath)
                .from(sale)
                .join(book).on(sale.book.id.eq(book.id))
                .where(searchDateFilterForBestSeller(currentDateTime.minusDays(7), currentDateTime))
                .groupBy(sale.book.id)
                .orderBy(sale.quantity.sum().desc())
                .limit(10)
                .fetch();

        return result;
    }

    private BooleanExpression eqBookName(String bookName) {

        if (StringUtils.isEmpty(bookName)) {
            return null;
        }

        return book.bookName.eq(bookName);
    }

    private BooleanExpression eqStatus(BookStatus bookStatus) {

        if (StringUtils.isEmpty(bookStatus)) {
            return null;
        }

        return book.status.eq(bookStatus);
    }

    private OrderSpecifier<?> bookSort(Pageable page) {

        if (!page.getSort().isEmpty()) {

            for (Sort.Order order : page.getSort()) {

                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "bookName":
                        return new OrderSpecifier(direction, book.bookName);
                    case "price":
                        return new OrderSpecifier(direction, book.price);
                    case "createdAt":
                        return new OrderSpecifier(direction, book.createdAt);
                }
            }
        }

        return null;
    }


    private BooleanExpression searchDateFilterForBestSeller(LocalDateTime searchStartDate, LocalDateTime searchEndDate) {

        BooleanExpression isGoeStartDate = sale.createdAt.goe(LocalDateTime.of(LocalDate.from(searchStartDate), LocalTime.MIN));
        BooleanExpression isLoeEndDate = sale.createdAt.loe(LocalDateTime.of(LocalDate.from(searchEndDate), LocalTime.MAX).withNano(0));

        return Expressions.allOf(isGoeStartDate, isLoeEndDate);
    }

}
