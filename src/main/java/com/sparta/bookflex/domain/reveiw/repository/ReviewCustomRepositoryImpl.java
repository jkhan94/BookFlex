package com.sparta.bookflex.domain.reveiw.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.reveiw.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.sparta.bookflex.domain.book.entity.QBook.book;
import static com.sparta.bookflex.domain.reveiw.entity.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Review> getReviewsByBookName(String bookName, Pageable pageable) {
        List<Review> result = queryFactory.select(review)
                .from(review)
                .where(eqBookName(bookName))
                .orderBy(reviewSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(review.count())
                .from(review)
                .where(eqBookName(bookName))
                .fetchOne();

        return new PageImpl<>(result, pageable, count);
    }

    private BooleanExpression eqBookName(String bookName) {

        if (StringUtils.isEmpty(bookName)) {
            return null;
        }

        return book.bookName.eq(bookName);
    }

    private OrderSpecifier<?> reviewSort(Pageable page) {

        if (!page.getSort().isEmpty()) {

            for (Sort.Order order : page.getSort()) {

                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                return new OrderSpecifier(direction, review.createdAt);
            }
        }

        return null;
    }


}
