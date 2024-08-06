package com.sparta.bookflex.domain.shipment.dto;

import lombok.Getter;

@Getter
public class ShipmentReqDto {

    private String trackingNumber;
    private String carrier;
}
