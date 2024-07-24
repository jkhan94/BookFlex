package com.sparta.bookflex.domain.orderbook.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.orderbook.emuns.RefundStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
public class OrderItemRefund extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "refund_status")
    private RefundStatus refundStatus;

    @ManyToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @Builder
    public OrderItemRefund(BigDecimal amount, RefundStatus refundStatus, OrderItem orderItem) {
        this.amount = amount;
        this.refundStatus = refundStatus;
        this.orderItem = orderItem;
    }
}
