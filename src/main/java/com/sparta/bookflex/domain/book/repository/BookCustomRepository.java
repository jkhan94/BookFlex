package com.sparta.bookflex.domain.book.repository;

import com.querydsl.core.Tuple;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.entity.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface BookCustomRepository {

    Page<Book> findBooks (String bookName, BookStatus bookStatus, Pageable pageable);
    List<Tuple> findBestSeller(LocalDateTime currentDateTime);
}
