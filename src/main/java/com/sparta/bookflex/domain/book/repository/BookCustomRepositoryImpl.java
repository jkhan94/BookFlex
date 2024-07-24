package com.sparta.bookflex.domain.book.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
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

import java.util.List;

import static com.sparta.bookflex.domain.book.entity.QBook.book;

@Repository
@RequiredArgsConstructor
public class BookCustomRepositoryImpl implements BookCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Book> findBooks(String BookName, BookStatus bookStatus, Pageable pageable) {
        List<Book> result = queryFactory.select(book)
                .from(book)
                .where(eqStatus(bookStatus))
                .where(eqBookName(BookName))
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

}
