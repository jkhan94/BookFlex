package com.sparta.bookflex.domain.book.repository;

import com.sparta.bookflex.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface bookRepository extends JpaRepository<Book, Long> {
}
