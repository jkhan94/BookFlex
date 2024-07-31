package com.sparta.bookflex.domain.reveiw.dto;

import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.reveiw.entity.Review;
import com.sparta.bookflex.domain.user.entity.User;
import lombok.Getter;

@Getter
public class ReviewRequestDto {

    private String title;

    private String content;

    private String star;

    public Review toEntity(User user, Book book) {
        return Review.builder()
                .user(user)
                .book(book)
                .title(this.title)
                .content(this.content)
                .star(this.star)
                .build();
    }
}
