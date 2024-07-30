package com.sparta.bookflex.domain.payment.enums;

public enum PayType {
    CARD("카드"),
    BANK_TRANSFER("계좌 이체"),
    MOBILE_PAYMENT("모바일 결제"),
    VIRTUAL_ACCOUNT("가상 계좌"),
    PAYPAL("페이팔"),
    TOSS("토스"),
    KAKAO_PAY("카카오페이"),
    NAVER_PAY("네이버페이");

    private final String displayName;

    PayType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
