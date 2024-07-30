package com.sparta.bookflex.domain.coupon.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum CouponType {
    GRADE(CouponTypeCode.GRADE_COUPON),
    BIRTHDAY(CouponTypeCode.BIRTHDAY_COUPON),
    GENERAL(CouponTypeCode.GENERAL_COUPON);

    private final String couponType;

    CouponType(String couponType) {
        this.couponType = couponType;
    }

    @JsonCreator
    public static CouponType toJson(String couponType) {
        for (CouponType type : CouponType.values()) {
            if (type.getCouponType().equals(couponType)) {
                return type;
            }
        }
        return null;
    }

    @JsonValue
    public String getCouponType() {
        return couponType;
    }

    private static class CouponTypeCode {
        public static final String GRADE_COUPON = "등급쿠폰";
        public static final String BIRTHDAY_COUPON = "생일쿠폰";
        public static final String GENERAL_COUPON = "일반쿠폰";
    }
}