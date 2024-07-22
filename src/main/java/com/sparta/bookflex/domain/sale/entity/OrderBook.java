package com.sparta.bookflex.domain.sale.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.sale.Enum.SaleState;
import jakarta.persistence.*;
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
    @Column(name = "status")
    private SaleState status;

    @OneToMany(mappedBy = "orderBook", cascade = CascadeType.ALL)
    private List<Sale> saleList;


}
