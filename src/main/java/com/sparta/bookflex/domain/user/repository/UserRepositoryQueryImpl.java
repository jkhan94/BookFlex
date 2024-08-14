package com.sparta.bookflex.domain.user.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.user.entity.QUser;
import com.sparta.bookflex.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.sparta.bookflex.domain.book.entity.QBook.book;
import static com.sparta.bookflex.domain.sale.entity.QSale.sale;
import static com.sparta.bookflex.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryQueryImpl implements UserRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<User> findByUserName(String username) {
        QUser quser = user;
        return Optional.ofNullable(jpaQueryFactory
                .select(quser)
                .from(quser)
                .where(quser.username.eq(username))
                .fetchOne());
    }

    public Page<Tuple> getUsers(String username, Pageable pageable) {
        List<Tuple> result = jpaQueryFactory.select(user.id, user.createdAt, user.username, user.name, user.grade, user.state)
                .from(user)
                .where(eqUsername(username))
                .orderBy(user.id.asc())
                .groupBy(user.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(user.count())
                .from(user)
                .where(eqUsername(username))
                .orderBy(user.id.asc())
                .fetchOne();

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public BigDecimal getSaleAmount(Long userId, LocalDateTime currentDateTime) {
        BigDecimal result = jpaQueryFactory.select(sale.total.sum())
                .from(user)
                .join(sale).on(sale.user.id.eq(user.id))
                .where(user.id.eq(userId))
                .where(sale.status.eq(OrderState.SALE_COMPLETED))
                .where(searchDateFilter(currentDateTime.minusDays(90), currentDateTime))
                .groupBy(user.id)
                .fetchOne();

        return result;
    }

    private BooleanExpression eqUsername(String username) {

        if (StringUtils.isEmpty(username)) {
            return null;
        }

        return user.username.eq(username);
    }

    private BooleanExpression searchDateFilter(LocalDateTime searchStartDate, LocalDateTime searchEndDate) {

        BooleanExpression isGoeStartDate = sale.createdAt.goe(LocalDateTime.of(LocalDate.from(searchStartDate), LocalTime.MIN));
        BooleanExpression isLoeEndDate = sale.createdAt.loe(LocalDateTime.of(LocalDate.from(searchEndDate), LocalTime.MAX).withNano(0));

        return Expressions.allOf(isGoeStartDate, isLoeEndDate);
    }


}
