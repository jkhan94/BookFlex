package com.sparta.bookflex.domain.book.repository;

import com.sparta.bookflex.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByBookName(String bookName);



}
