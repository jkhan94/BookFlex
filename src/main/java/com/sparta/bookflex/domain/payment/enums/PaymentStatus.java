package com.sparta.bookflex.domain.payment.enums;

public enum PaymentStatus {
    PAY_STANDBY("결제 대기 중"),
    PAY_APPROVED("구매자 인증 완료"),
    PAY_CANCEL("결제 취소"),
    PAY_PROGRESS("결제 진행 중"),
    PAY_COMPLETE("결제 완료"),
    REFUND_PROGRESS("환불 진행 중"),
    REFUND_SUCCESS("환불 성공"),
    SETTLEMENT_COMPLETE("정산 완료"),
    SETTLEMENT_REFUND_COMPLETE("환불 정산 완료");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
