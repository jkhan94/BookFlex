package com.sparta.bookflex.domain.category.repository;

import com.sparta.bookflex.domain.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryQRepository {
    Page<Book> findAllBooks(long categoryId, Pageable pageable);
}
