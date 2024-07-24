package com.sparta.bookflex.domain.reveiw.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.reveiw.ReviewRequestDto;
import com.sparta.bookflex.domain.reveiw.ReviewResponseDto;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String star;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Builder
    public Review(String title, String content, String star, User user, Book book) {
        this.title = title;
        this.content = content;
        this.star = star;
        this.user = user;
        this.book = book;
    }

    public ReviewResponseDto toResponseDto() {
        return ReviewResponseDto.builder()
                .title(this.title)
                .content(this.content)
                .star(this.star)
                .username(this.user.getUsername())
                .bookName(this.book.getBookName())
                .createdAt(this.createdAt)
                .modifiedAt(this.modifiedAt)
                .build();
    }

    public void update(ReviewRequestDto reviewRequestDto) {
        this.title = reviewRequestDto.getTitle();
        this.content = reviewRequestDto.getContent();
        this.star = reviewRequestDto.getStar();
    }
}
