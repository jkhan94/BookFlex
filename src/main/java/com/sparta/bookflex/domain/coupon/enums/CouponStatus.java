package com.sparta.bookflex.domain.coupon.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum CouponStatus {
    NotAvailable(CouponStatusCode.NotAvailable),
    Available(CouponStatusCode.Available);

    private final String status;

    private CouponStatus(String status) {
        this.status = status;
    }

    @JsonCreator
    public static CouponStatus toJson(String qnaType) {
        for (CouponStatus type : CouponStatus.values()) {
            if (type.getStatus().equals(qnaType)) {
                return type;
            }
        }
        return null;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }

    private static class CouponStatusCode {
        public static final String NotAvailable = "발급불가";
        public static final String Available = "발급가능";
    }
}
