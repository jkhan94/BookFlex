package com.sparta.bookflex.domain.coupon.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum DiscountType {
    PERCENTAGE(DiscountTypeCode.PERCENTAGE_DISCOUNT),
    FIXED_AMOUNT(DiscountTypeCode.FIXED_AMOUNT_DISCOUNT);

    private final String discountType;

    DiscountType(String couponType) {
        this.discountType = couponType;
    }

    @JsonCreator
    public static DiscountType toJson(String discountType) {
        for (DiscountType type : DiscountType.values()) {
            if (type.getDiscountType().equals(discountType)) {
                return type;
            }
        }
        return null;
    }

    @JsonValue
    public String getDiscountType() {
        return discountType;
    }

    private static class DiscountTypeCode {
        public static final String PERCENTAGE_DISCOUNT = "일정 비율 할인";
        public static final String FIXED_AMOUNT_DISCOUNT = "일정 금액 할인";
    }
}
