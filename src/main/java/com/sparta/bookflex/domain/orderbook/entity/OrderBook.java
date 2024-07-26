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

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "orderBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList;

    @OneToMany(mappedBy = "orderBook")
    private List<Sale> saleList;



    @Builder
    public OrderBook(OrderState status, BigDecimal total, User user) {
        this.status = status;
        this.total = total;
        this.user = user;
    }

    public void updateSaleList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public void updateStatus(OrderState status) {
        this.status = status;
    }
}
