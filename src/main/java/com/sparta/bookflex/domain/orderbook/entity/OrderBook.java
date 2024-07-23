package com.sparta.bookflex.domain.orderbook.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.sale.Enum.SaleState;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class OrderBook extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private SaleState status;

    @Column(name = "total")
    private int total;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "orderBook", cascade = CascadeType.ALL)
    private List<Sale> saleList;

    @Builder
    public OrderBook(SaleState status, int total, User user) {
        this.status = status;
        this.total = total;
        this.user = user;
    }

    public void updateSaleList(List<Sale> sales) {
        this.saleList = sales;
    }
}
