package com.sparta.bookflex.domain.shipment.dto;

import com.sparta.bookflex.domain.shipment.enums.ShipmentEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ShipmentResDto {

    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private String carrierName;
    private String trackingNumber;
    private ShipmentEnum status;
    private String orderNo;
    private String username;

    @Builder
    public ShipmentResDto(LocalDateTime shippedAt, LocalDateTime deliveredAt, String carrierName, ShipmentEnum status, String orderNo, String username, String trackingNumber) {
        this.shippedAt = shippedAt;
        this.deliveredAt = deliveredAt;
        this.carrierName = carrierName;
        this.status = ShipmentEnum.PENDING;
        this.orderNo = orderNo;
        this.username = username;
        this.trackingNumber = trackingNumber;
    }

    public void updateStatus(String name){

        if(name.equalsIgnoreCase("DELIVERED")) {
            status = ShipmentEnum.IN_DELIVERY;
        }
        else if(name.equalsIgnoreCase("IN_TRANSIT") || name.equalsIgnoreCase("In Transit")) {
            status = ShipmentEnum.IN_TRANSIT;
        }
        else {
            status = ShipmentEnum.DELIVERED;
        }
    }

//    public ShipmentResDto(String time, String name) {
//        String cleanedDateTimeString;
//        if(time.contains("+")){
//            cleanedDateTimeString = time.replace("T", " ").substring(0, time.indexOf("+"));
//        }
//        else {
//            cleanedDateTimeString = time.replace("T", " ").replace("Z","");
//        }
//        shippedAt = LocalDateTime.parse(cleanedDateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
//        if(name.equalsIgnoreCase("DELIVERED")) {
//            status = ShipmentEnum.DELIVERED;
//        }
//        else if(name.equalsIgnoreCase("IN_TRANSIT") || name.equalsIgnoreCase("In Transit")) {
//            status = ShipmentEnum.IN_TRANSIT;
//        }
//        else {
//            status = ShipmentEnum.CANCELED;
//        }
//    }
}