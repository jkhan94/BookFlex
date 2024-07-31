package com.sparta.bookflex.domain.orderbook.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class OrderBook extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_book_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private OrderState status;

    @Column(name = "total",precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "order_no")
    private String orderNo;


    @Column(name = "discount",precision = 10, scale = 2)
    private BigDecimal discount;

    @Column(name = "discount_total",precision = 10, scale = 2)
    private BigDecimal discountTotal;

    @Column(name = "is_coupon")
    private boolean isCoupon;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "orderBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList;

    @OneToMany(mappedBy = "orderBook")
    private List<Sale> saleList;

    private final String PREFIX = "BookFlexA";



    @Builder
    public OrderBook(OrderState status, BigDecimal total, User user, BigDecimal discountPrice,String orderNo) {
        this.status = status;
        this.user = user;
        this.discount = discountPrice != null ? discountPrice : BigDecimal.ZERO;
        this.total = total ;
        this.orderNo = orderNo;
        this.discountTotal = total.subtract(discountPrice != null ? discountPrice : BigDecimal.ZERO);
        this.isCoupon = false;
    }

    public void updateSaleList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public void updateStatus(OrderState status) {
        this.status = status;
    }

    public void updateDiscount(BigDecimal discount) {
        this.discount = discount;
        this.discountTotal = this.total.subtract(discount);
        this.isCoupon = true;
    }

    public void generateOrderNo() {
        if (this.id != null) {
            this.orderNo = String.format("%s-%d", PREFIX, this.id);
        }
    }
}
