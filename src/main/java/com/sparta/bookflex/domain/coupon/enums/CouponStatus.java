package com.sparta.bookflex.domain.coupon.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum CouponStatus {
    NOT_AVAILABLE(CouponStatusCode.NOT_AVAILABLE_CODE),
    AVAILABLE(CouponStatusCode.AVAILABLE_CODE);

    private final String status;

    private CouponStatus(String status) {
        this.status = status;
    }

    @JsonCreator
    public static CouponStatus toJson(String couponStatus) {
        for (CouponStatus type : CouponStatus.values()) {
            if (type.getStatus().equals(couponStatus)) {
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
        public static final String NOT_AVAILABLE_CODE = "발급불가";
        public static final String AVAILABLE_CODE = "발급가능";
    }
}
