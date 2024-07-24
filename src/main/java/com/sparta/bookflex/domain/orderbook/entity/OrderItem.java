package com.sparta.bookflex.domain.orderbook.entity;


import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.sale.Enum.SaleState;
import com.sparta.bookflex.domain.shipment.entity.Shipment;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemRefund> orderItemRefunds = new ArrayList<>();


    @Builder
    public OrderItem(int quantity, Book book, User user, OrderBook orderBook,BigDecimal price,Shipment shipment) {
        this.quantity = quantity;
        this.book = book;
        this.user = user;
        this.price = price;
        this.total = BigDecimal.valueOf(quantity).multiply(price);
        this.orderBook = orderBook;
        this.shipment = shipment;

    }

    public void updateShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public void updateOrderBook(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public void addRefund(OrderItemRefund refund) {
        this.orderItemRefunds.add(refund);
    }

}




