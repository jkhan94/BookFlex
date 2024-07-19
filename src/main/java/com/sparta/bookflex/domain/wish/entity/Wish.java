package com.sparta.bookflex.domain.wish.entity;

import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name="book_id", nullable = false)
    private Book book;

    @OneToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

}
