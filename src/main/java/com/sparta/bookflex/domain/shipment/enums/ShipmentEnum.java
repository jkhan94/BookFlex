package com.sparta.bookflex.domain.shipment.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ShipmentEnum {
    PENDING("배송 대기 중"),       // 배송 대기 중
    PROCESSING("처리 중"),    // 처리 중
    SHIPPED("배송됨"),       // 배송됨
    IN_TRANSIT("운송 중"),    // 운송 중
    IN_DELIVERY("배송 중"), // 배송 중
    DELIVERED("배송 완료"),     // 배송 완료
    RETURNED("반품됨"),      // 반품됨
    CANCELED("취소됨");

    @JsonValue
    private final String desscription;

    ShipmentEnum(String desscription){
        this.desscription = desscription;
    }
}
