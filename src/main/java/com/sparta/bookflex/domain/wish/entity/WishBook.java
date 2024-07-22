package com.sparta.bookflex.domain.wish.entity;

import com.sparta.bookflex.domain.book.entity.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class WishBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="wish_id")
    private Wish wish;

    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

}
