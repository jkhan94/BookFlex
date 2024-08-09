package com.sparta.bookflex.domain.shipment.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class TotalShipmentResDto {
    Page<ShipmentResDto> shipmentResDtos;
    long totalCount;

    public TotalShipmentResDto(Page<ShipmentResDto> shipmentResDtos, long totalCount) {
        this.shipmentResDtos = shipmentResDtos;
        this.totalCount = totalCount;
    }
}
