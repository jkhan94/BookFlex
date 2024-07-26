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
    private ShipmentEnum status;

    public ShipmentResDto(String time, String name) {
        String cleanedDateTimeString = time.replace("T", " ").substring(0, time.indexOf("+"));
        shippedAt = LocalDateTime.parse(cleanedDateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        if(name.equals("DELIVERED")) {
            status = ShipmentEnum.DELIVERED;
        }
        else if(name.equals("IN_TRANSIT")) {
            status = ShipmentEnum.IN_TRANSIT;
        }
        else {
            status = ShipmentEnum.CANCELED;
        }
    }
}
