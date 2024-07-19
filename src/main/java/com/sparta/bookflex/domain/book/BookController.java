package com.sparta.bookflex.domain.book;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public void registerProduct(@RequestBody BookReqeustDto bookReqeustDto) {

        return bookService.registerProduct(bookReqeustDto);


    }

}
