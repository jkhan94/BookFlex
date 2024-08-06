package com.sparta.bookflex.domain.sale.entity;


import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.orderbook.entity.OrderItem;
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
    private OrderState status;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "discount", nullable = false)
    private BigDecimal discount;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_book_id")
    private OrderBook orderBook;



    @Builder
    public Sale(OrderState status, int quantity, Book book, User user,BigDecimal price,BigDecimal total,OrderBook orderBook,BigDecimal discount) {
        this.status = status;
        this.discount = discount == null ? BigDecimal.ZERO : discount;
        this.quantity = quantity;
        this.book = book;
        this.user = user;
        this.price = price;
        this.total = total.subtract(discount != null?discount:BigDecimal.ZERO);
        this.orderBook = orderBook;
    }

    public Sale(OrderItem orderItem,OrderState status) {
        this.status = status;
        this.quantity = orderItem.getQuantity();
        this.book = orderItem.getBook();
        this.user = orderItem.getOrderBook().getUser();
        this.price = orderItem.getPrice();
        this.total = orderItem.getPrice();
        this.orderBook = orderItem.getOrderBook();
    }

    public void updateStatus(OrderState status) {
        this.status = status;
    }

    public void updateDiscount(BigDecimal discount) {
        this.discount = discount;
        this.total = this.total.subtract(discount);
    }
}




