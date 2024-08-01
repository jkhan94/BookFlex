package com.sparta.bookflex.domain.shipment.dto;

import com.sparta.bookflex.domain.shipment.enums.ShipmentEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class ShipmentResDto {

    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private String carrierName;
    private ShipmentEnum status;

    public ShipmentResDto(LocalDateTime shipedAt, LocalDateTime deliverdAt, ShipmentEnum status) {
        this.shippedAt = shipedAt;
        this.deliveredAt = deliverdAt;
        this.carrierName = "dev.track.dummy";
        this.status = status;
    }

    public ShipmentResDto(String time, String name) {
        String cleanedDateTimeString;
        if(time.contains("+")){
            cleanedDateTimeString = time.replace("T", " ").substring(0, time.indexOf("+"));
        }
        else {
            cleanedDateTimeString = time.replace("T", " ").replace("Z","");
        }
        shippedAt = LocalDateTime.parse(cleanedDateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        if(name.equalsIgnoreCase("DELIVERED")) {
            status = ShipmentEnum.DELIVERED;
        }
        else if(name.equalsIgnoreCase("IN_TRANSIT") || name.equalsIgnoreCase("In Transit")) {
            status = ShipmentEnum.IN_TRANSIT;
        }
        else {
            status = ShipmentEnum.CANCELED;
        }
    }
}