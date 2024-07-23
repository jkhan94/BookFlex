package com.sparta.bookflex.domain.sale.entity;


import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.sale.Enum.SaleState;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "total", nullable = false)
    private int total;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderBook orderBook;


    @Builder
    public Sale(SaleState status, int quantity, Book book, User user,OrderBook orderBook) {
        this.status = status;
        this.quantity = quantity;
        this.book = book;
        this.user = user;
        this.total = quantity * (book != null ? book.getPrice() : 0);
        this.orderBook = orderBook;

    }

    public void updateOrderBook(OrderBook orderBook) {
        this.orderBook = orderBook;
    }
}




