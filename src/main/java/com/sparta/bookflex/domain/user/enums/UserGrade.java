package com.sparta.bookflex.domain.user.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.sparta.bookflex.domain.coupon.enums.CouponType;

public enum UserGrade {
    NORMAL(UserGradeCode.NORMAL),
    VIP(UserGradeCode.VIP);

    private final String userGrade;

    UserGrade(String userGrade) {
        this.userGrade = userGrade;
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
    public String getUserGrade() {
        return userGrade;
    }

    private static class UserGradeCode {
        public static final String NORMAL = "일반회원";
        public static final String VIP = "VIP";
    }
}
