package com.sparta.bookflex.domain.sale.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bookflex.domain.category.enums.Category;
import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.sparta.bookflex.domain.book.entity.QBook.book;
import static com.sparta.bookflex.domain.sale.entity.QSale.sale;

@RequiredArgsConstructor
@Repository
public class SaleQRepositoryImpl implements SaleQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Sale> findAllSalesByUser(User user, Pageable pageable) {
        List<Sale> sales = queryFactory.selectFrom(sale)
                .where(sale.user.eq(user))
                .orderBy(sale.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count query
        List<Sale> count = queryFactory.selectFrom(sale)
                .where(sale.user.eq(user))
                .fetch();

        return new PageImpl<>(sales, pageable, count.size());
    }

    @Override
    public Page<Tuple> findSaleByBookName(String bookName, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        List<Tuple> result = queryFactory
                .select(book.bookName, book.subCategory, book.publisher, book.author, book.price, sale.quantity.sum(), sale.total.sum())
                .from(sale)
                .join(book).on(sale.book.id.eq(book.id))
                .where(eqBookName(bookName))
                .where(sale.status.eq(OrderState.SALE_COMPLETED))
                .where(searchDateFilter(startDate, endDate))
                .groupBy(book.bookName, book.price, book.subCategory, book.publisher, book.author)
                .orderBy(tupleSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Tuple> count = queryFactory
                .select(book.bookName, book.subCategory, book.publisher, book.author, sale.price, sale.quantity.sum(), sale.total.sum())
                .from(sale)
                .join(book).on(sale.book.id.eq(book.id))
                .where(eqBookName(bookName))
                .where(sale.status.eq(OrderState.SALE_COMPLETED))
                .where(searchDateFilter(startDate, endDate))
                .groupBy(book.bookName, book.price, book.subCategory, book.publisher, book.author)
                .fetch();

        return new PageImpl<>(result, pageable, count.size());
    }

    @Override
    public Page<Tuple> findSaleByCategory(String categoryName, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        List<Tuple> result = queryFactory
                .select(book.subCategory, sale.quantity.sum(), sale.total.sum())
                .from(sale)
                .join(book).on(sale.book.id.eq(book.id))
                .where(eqCategoryName(categoryName))
                .where(sale.status.eq(OrderState.SALE_COMPLETED))
                .where(searchDateFilter(startDate, endDate))
                .groupBy(book.subCategory)
                .orderBy(tupleSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Tuple> count = queryFactory
                .select(book.subCategory, sale.total.sum())
                .from(sale)
                .join(book).on(sale.book.id.eq(book.id))
                .where(eqCategoryName(categoryName))
                .where(sale.status.eq(OrderState.SALE_COMPLETED))
                .where(searchDateFilter(startDate, endDate))
                .groupBy(book.subCategory)
                .fetch();

        return new PageImpl<>(result, pageable, count.size());
    }

    private BooleanExpression eqBookName(String bookName) {
        if (bookName == null || bookName.isEmpty()) {
            return null;
        }
        return book.bookName.eq(bookName);
    }

    private BooleanExpression eqCategoryName(String categoryName) {
        if (categoryName == null || categoryName.isEmpty()) {
            return null;
        }
        Category category = Category.of(categoryName);
        return book.subCategory.eq(category);
    }

    private BooleanExpression searchDateFilter(LocalDate searchStartDate, LocalDate searchEndDate) {

        BooleanExpression isGoeStartDate = sale.createdAt.goe(LocalDateTime.of(searchStartDate, LocalTime.MIN));
        BooleanExpression isLoeEndDate = sale.createdAt.loe(LocalDateTime.of(searchEndDate, LocalTime.MAX).withNano(0));

        return Expressions.allOf(isGoeStartDate, isLoeEndDate);
    }

    private OrderSpecifier<?> tupleSort(Pageable page) {

        if (!page.getSort().isEmpty()) {

            for (Sort.Order order : page.getSort()) {

                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "bookName":
                        return new OrderSpecifier(direction, book.bookName);
                    case "total":
                        return new OrderSpecifier(direction, sale.total.sum());
                    case "category":
                        return new OrderSpecifier(direction, book.subCategory);
                }
            }
        }

        return null;
    }

}
