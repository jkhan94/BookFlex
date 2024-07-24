package com.sparta.bookflex.domain.sale.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
