package com.sparta.bookflex.domain.coupon.dto;

import com.sparta.bookflex.domain.coupon.enums.DiscountType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CouponOrderResponseDto {
    private Long userCouponId;
    private String couponCode;
    private String couponName;
    private DiscountType discountType;
    private BigDecimal minPrice;
    private BigDecimal discountPrice;


    @Builder
    public CouponOrderResponseDto(Long userCouponId, String couponCode, DiscountType discountType, BigDecimal minPrice, BigDecimal discountPrice, String couponName) {
        this.couponName = couponName;
        this.userCouponId = userCouponId;
        this.couponCode = couponCode;
        this.discountType = discountType;
        this.minPrice = minPrice;
        this.discountPrice = discountPrice;
    }
}
