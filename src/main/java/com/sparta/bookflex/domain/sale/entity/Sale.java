package com.sparta.bookflex.domain.sale.entity;


import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.orderbook.entity.OrderItem;
import com.sparta.bookflex.domain.sale.Enum.SaleState;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "sale")
@NoArgsConstructor
public class Sale extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long id;

    @Column(name = "sale_date")
    private String saleDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SaleState status;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



    @Builder
    public Sale(SaleState status, int quantity, Book book, User user,BigDecimal price,BigDecimal total) {
        this.status = status;
        this.quantity = quantity;
        this.book = book;
        this.user = user;
        this.price = price;
        this.total = total;
    }

    public Sale(OrderItem orderItem,SaleState status) {
        this.status = status;
        this.quantity = orderItem.getQuantity();
        this.book = orderItem.getBook();
        this.user = orderItem.getOrderBook().getUser();
        this.price = orderItem.getPrice();
        this.total = orderItem.getPrice();
    }

    public void updateStatus(SaleState status) {
        this.status = status;
    }
}




