package com.sparta.bookflex.domain.orderbook.entity;


import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.shipment.entity.Shipment;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "order_item")
@NoArgsConstructor
public class OrderItem extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", precision = 12, scale = 2,nullable = false)
    private BigDecimal price;

    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "order_book_id")
    private OrderBook orderBook;


    @Builder
    public OrderItem(int quantity, Book book,  OrderBook orderBook,BigDecimal price) {
        this.quantity = quantity;
        this.book = book;
        this.price = price;
        this.total = BigDecimal.valueOf(quantity).multiply(price);
        this.orderBook = orderBook;
    }



    public void updateOrderBook(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

}




