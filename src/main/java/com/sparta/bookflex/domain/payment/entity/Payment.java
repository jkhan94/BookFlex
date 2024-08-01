package com.sparta.bookflex.domain.payment.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.payment.enums.PayType;
import com.sparta.bookflex.domain.payment.enums.PaymentStatus;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total", nullable = false)
    private int total;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_type", nullable = false)
    private PayType payType;

    @Column(nullable = false)
    private PaymentStatus status;

    @OneToOne
    @JoinColumn(name = "order_book_id", nullable = false)
    private OrderBook orderBook;

    private boolean paySuccessYN;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "pay_token")
    private String payToken;

    @Column(name = "fail_reason")
    private String failReason;

    @Column(name = "cancel_yn")
    private boolean cancelYN;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "discount")
    private BigDecimal discount;

    @Builder
    public Payment(BigDecimal total, PayType payType, PaymentStatus status, OrderBook orderBook, boolean paySuccessYN, User user, String payToken, String failReason, boolean cancelYN, String cancelReason, BigDecimal discount) {
        this.discount = discount != null ? discount : BigDecimal.ZERO;
        this.payType = payType;
        this.status = status;
        this.orderBook = orderBook;
        this.paySuccessYN = paySuccessYN;
        this.user = user;
        this.payToken = payToken;
        this.failReason = failReason;
        this.cancelYN = cancelYN;
        this.cancelReason = cancelReason;
        this.total = total.subtract(discount != null? discount: BigDecimal.ZERO).intValue();
    }

    public void updateStatus(PaymentStatus status) {
        this.status = status;

    }

    public void updateIsSuccessYN(boolean b) {
        this.paySuccessYN = b;
    }
}
