package com.sparta.bookflex.domain.book;

import com.sparta.bookflex.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Book;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public void registerProduct(BookReqeustDto bookReqeustDto) {
        Book book = bookReqeustDto.toEntity();
    }
}
