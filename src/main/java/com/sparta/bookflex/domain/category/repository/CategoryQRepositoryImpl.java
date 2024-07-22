package com.sparta.bookflex.domain.category.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bookflex.domain.book.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.sparta.bookflex.domain.book.entity.QBook.book;
import static com.sparta.bookflex.domain.category.entity.QCategory.category;

@RequiredArgsConstructor
public class CategoryQRepositoryImpl implements CategoryQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Book> findAllBooks(long categoryId, Pageable pageable) {
        List<Book> result = queryFactory.select(book)
                .from(book)
                .leftJoin(category).on(category.id.eq(book.category.id))
                .fetchJoin()
                .where(book.category.id.eq(categoryId))
                .orderBy(book.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Book> count = queryFactory.select(book)
                .from(book)
                .leftJoin(category).on(category.id.eq(book.category.id))
                .fetchJoin()
                .where(book.category.id.eq(categoryId))
                .fetch();

        return new PageImpl<>(result, pageable, count.size());
    }
}
