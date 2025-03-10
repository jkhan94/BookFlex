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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.sparta.bookflex.domain.book.entity.QBook.book;
import static com.sparta.bookflex.domain.sale.entity.QSale.sale;
import static com.sparta.bookflex.domain.user.entity.QUser.user;

@RequiredArgsConstructor
@Repository
public class SaleQRepositoryImpl implements SaleQRepository {

    private final JPAQueryFactory queryFactory;
    private final JPAQueryFactory jpaQueryFactory;

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
                .select(book.bookName, book.subCategory, book.publisher, book.author, book.price, sale.quantity.sum(), sale.total.sum())
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

    @Override
    public Page<Tuple> findSales(String username, String status, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        List<Tuple> result = queryFactory
                .select(sale.createdAt, sale.id, user.username, sale.book.id, sale.status, sale.total, sale.quantity, sale.price)
                .from(sale)
                .join(user).on(sale.user.id.eq(user.id))
                .where(eqUserName(username))
                .where(eqStatus(status))
                .where(searchDateFilter(startDate, endDate))
                .orderBy(tupleSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Tuple> count = queryFactory
                .select(sale.createdAt, sale.id, user.username, sale.book.id, sale.status)
                .from(sale)
                .join(user).on(sale.user.id.eq(user.id))
                .where(eqUserName(username))
                .where(eqStatus(status))
                .where(searchDateFilter(startDate, endDate))
                .orderBy(tupleSort(pageable))
                .fetch();

        return new PageImpl<>(result, pageable, count.size());
    }



    @Override
    public List<Sale> findAllSalesStatusChagable() {
        List<Sale> result = queryFactory
            .select(sale)
            .from(sale)
            .where(chageableStatus())
            .fetch();

        return result;
    }

    private BooleanExpression chageableStatus() {
        Collection<OrderState> states = new ArrayList<>();
        int count = 0;
        for(OrderState state : OrderState.values()) {
            if(count > 2) {
                break;
            }
            states.add(state);
            count++;
        }
        return sale.status.in(states);
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
        Category category = Category.findCategoryByName(categoryName);
        return book.subCategory.eq(category);
    }

    private BooleanExpression eqUserName(String userName) {
        if (userName == null || userName.isEmpty()) {
            return null;
        }

        return sale.user.username.eq(userName);
    }

    private BooleanExpression eqStatus(String status) {

        if (status == null || status.isEmpty()) {
            return null;
        }

        switch (status) {
            case "pendingPayment":
                return sale.status.eq(OrderState.PENDING_PAYMENT);
            case "itemPrepairng":
                return sale.status.eq(OrderState.ITEM_PREPARING);
            case "inDelivery":
                return sale.status.eq(OrderState.IN_DELIVERY);
            case "deliveryCompleted":
                return sale.status.eq(OrderState.DELIVERY_COMPLETED);
            case "saleCompleted":
                return sale.status.eq(OrderState.SALE_COMPLETED);
            case "orderCancelled":
                return sale.status.eq(OrderState.ORDER_CANCELLED);
            case "refundProcessing":
                return sale.status.eq(OrderState.REFUND_PROCESSING);
            default:
                return null;
        }
    }

    private BooleanExpression searchDateFilter(LocalDate searchStartDate, LocalDate searchEndDate) {

        BooleanExpression isGoeStartDate = sale.createdAt.goe(LocalDateTime.of(searchStartDate, LocalTime.MIN));
        BooleanExpression isLoeEndDate = sale.createdAt.loe(LocalDateTime.of(searchEndDate, LocalTime.MAX).withNano(0));

        return Expressions.allOf(isGoeStartDate, isLoeEndDate);
    }

    private BooleanExpression searchDateFilterForBestSeller(LocalDateTime searchStartDate, LocalDateTime searchEndDate) {

        BooleanExpression isGoeStartDate = sale.createdAt.goe(LocalDateTime.of(LocalDate.from(searchStartDate), LocalTime.MIN));
        BooleanExpression isLoeEndDate = sale.createdAt.loe(LocalDateTime.of(LocalDate.from(searchEndDate), LocalTime.MAX).withNano(0));

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
                    case "createdAt":
                        return new OrderSpecifier(direction, sale.createdAt);
                }
            }
        }

        return null;
    }


    public List<BigDecimal> getTotalAmount(Long userId) {
        List<BigDecimal> result = new ArrayList<>();
        result = jpaQueryFactory.select(sale.total.sum())
                .from(sale)
                .where(sale.user.id.eq(userId))
                .groupBy(sale.user.id)
                .fetch();

        if(result.isEmpty()){
           result.add(BigDecimal.ZERO);
        }
        return result;
    }
}
