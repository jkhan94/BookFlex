package com.sparta.bookflex.domain.coupon.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserCouponResponseDto {
    private String couponCode;
    private Boolean isUsed;
    private LocalDateTime usedAt;
    private CouponResponseDto coupon;

    @Builder
    public UserCouponResponseDto(String couponCode, Boolean isUsed, LocalDateTime usedAt, CouponResponseDto coupon) {
        this.couponCode = couponCode;
        this.isUsed = isUsed;
        this.usedAt = usedAt;
        this.coupon = coupon;
    }
}
