package com.sparta.bookflex.domain.payment.enums;

public enum PaymentStatus {
    PENDING("결제 대기 중"),
    COMPLETED("결제 완료"),
    PARTIALLY_REFUNDED("부분 환불 완료"),
    REFUNDED("전체 환불 완료"),
    FAILED("결제 실패"),
    CANCELLED("결제 취소");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
