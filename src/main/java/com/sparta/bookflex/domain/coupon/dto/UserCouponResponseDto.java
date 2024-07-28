package com.sparta.bookflex.domain.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserCouponResponseDto {
    private String couponCode;
    private Boolean isUsed;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
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
