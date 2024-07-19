package com.sparta.bookflex.domain.sale.entity;


import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Sale extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "total", nullable = false)
    private int total;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Builder
    public Sale(String status, int quantity, Book book, User user) {
        this.status = status;
        this.quantity = quantity;
        this.book = book;
        this.user = user;
    }
}




