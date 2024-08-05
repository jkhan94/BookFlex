package com.sparta.bookflex.domain.orderbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderShipResDto {

    private String orderNumber;
    private String username;
    private String trackingNumber;
    private String carrier;

    public OrderShipResDto(String orderNumber, String username, String trackingNumber, String carrier) {
        this.orderNumber = orderNumber;
        this.username = username;
        this.trackingNumber = trackingNumber;
        this.carrier = carrier;
    }
}
