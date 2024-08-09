package com.sparta.bookflex.domain.shipment.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.shipment.dto.ShipmentResDto;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    public Shipment(String trackingNumber, String carrier, LocalDateTime shippedAt, LocalDateTime deliveredAt, OrderBook orderBook,User user,String address) {
        this.trackingNumber = trackingNumber;
        this.carrier = carrier;
        this.shippedAt = shippedAt;
        this.deliveredAt = deliveredAt;
        this.orderBook = orderBook;
        this.user = user;
        this.address = address;
    }

    public static ShipmentResDto toShipmentResDto(Shipment shipment){
        String orderNo = "";
        if(shipment.orderBook == null) {
            orderNo = "NoData";
        } else {
            orderNo = shipment.orderBook.getOrderNo();
        }
        return ShipmentResDto.builder()
            .username(shipment.getUser().getUsername())
            .orderNo(orderNo)
            .carrierName(shipment.getCarrier())
            .trackingNumber(shipment.getTrackingNumber())
            .shippedAt(shipment.getShippedAt())
            .deliveredAt(shipment.getDeliveredAt())
            .build();
    }
}
