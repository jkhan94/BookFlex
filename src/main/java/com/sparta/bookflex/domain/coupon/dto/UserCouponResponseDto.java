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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime issuedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime expirationDate;

    private Boolean isUsed;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime usedAt;

    private CouponResponseDto coupon;

    @Builder

    public UserCouponResponseDto(String couponCode, LocalDateTime issuedAt, LocalDateTime expirationDate, Boolean isUsed,
                                 LocalDateTime usedAt, CouponResponseDto coupon) {
        this.couponCode = couponCode;
        this.issuedAt = issuedAt;
        this.expirationDate = expirationDate;
        this.isUsed = isUsed;
        this.usedAt = usedAt;
        this.coupon = coupon;
    }
}
