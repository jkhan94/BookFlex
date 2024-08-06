package com.sparta.bookflex.domain.shipment.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.orderbook.entity.OrderItem;
import com.sparta.bookflex.domain.shipment.enums.ShipmentEnum;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Shipment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Long id;

    @Column(name = "tracking_number",nullable = false)
    private String trackingNumber;

    @Column(name = "carrier",nullable = false)
    private String carrier;

    @Column(name = "status",nullable = false)
    private ShipmentEnum status;

    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToOne(mappedBy = "shipment")
    private OrderBook orderBook;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Builder
    public Shipment(String trackingNumber, String carrier, ShipmentEnum status, LocalDateTime shippedAt, LocalDateTime deliveredAt, OrderBook orderBook,User user,String address) {
        this.trackingNumber = trackingNumber;
        this.carrier = carrier;
        this.status = status;
        this.shippedAt = shippedAt;
        this.deliveredAt = deliveredAt;
        this.orderBook = orderBook;
        this.user = user;
        this.address = address;
    }
}
