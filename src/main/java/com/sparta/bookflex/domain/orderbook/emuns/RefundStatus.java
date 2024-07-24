package com.sparta.bookflex.domain.orderbook.emuns;

public enum RefundStatus {
    PENDING("환불 대기 중"),
    COMPLETED("환불 완료"),
    FAILED("환불 실패");

    private final String description;

    RefundStatus(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
