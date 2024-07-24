package com.sparta.bookflex.domain.book.repository;

import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.entity.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookCustomRepository {

    Page<Book> findBooks (String bookName, BookStatus bookStatus, Pageable pageable)     ;

}
