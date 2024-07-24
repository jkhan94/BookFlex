package com.sparta.bookflex.domain.payment.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.payment.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Payment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "price",precision = 12, scale = 2)
    private BigDecimal price;

    @Column
    private String paymentMethod;

    @Column(nullable = false)
    private PaymentStatus status;

    @Column(name = "refunded_amount", precision = 10, scale = 2)
    private BigDecimal refundAmount = BigDecimal.ZERO;

    @OneToOne
    @JoinColumn(name="order_book_id", nullable = false)
    private OrderBook orderBook;

    @Builder
    public Payment(BigDecimal price, String paymentMethod, PaymentStatus status, OrderBook orderbook) {
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.orderBook = orderbook;
    }

}
