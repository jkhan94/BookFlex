package com.sparta.bookflex.domain.shipment.enums;

public enum ShipmentEnum {
    PENDING,       // 배송 대기 중
    PROCESSING,    // 처리 중
    SHIPPED,       // 배송됨
    IN_TRANSIT,    // 운송 중
    OUT_FOR_DELIVERY, // 배달 중
    DELIVERED,     // 배송 완료
    RETURNED,      // 반품됨
    CANCELED;

    @Override
    public String toString() {
        switch(this) {
            case PENDING: return "배송 대기 중";
            case PROCESSING: return "처리 중";
            case SHIPPED: return "배송됨";
            case IN_TRANSIT: return "운송 중";
            case OUT_FOR_DELIVERY: return "배달 중";
            case DELIVERED: return "배송 완료";
            case RETURNED: return "반품됨";
            case CANCELED: return "취소됨";
            default: throw new IllegalArgumentException();
        }
    }// 취소됨
}
