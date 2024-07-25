package com.sparta.bookflex.domain.coupon.dto;

import com.sparta.bookflex.domain.coupon.enums.CouponStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CouponResponseDto {
    private Long couponId;
    private String couponName;
    private CouponStatus couponStatus;
    private int totalCount;
    private BigDecimal minPrice;
    private BigDecimal discountPrice;
    private LocalDateTime startDate;
    private LocalDateTime expirationDate;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public CouponResponseDto(Long couponId, String couponName, CouponStatus couponStatus, int totalCount, BigDecimal minPrice, BigDecimal discountPrice,
                             LocalDateTime startDate, LocalDateTime expirationDate, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.couponId = couponId;
        this.couponName = couponName;
        this.couponStatus = couponStatus;
        this.totalCount = totalCount;
        this.minPrice = minPrice;
        this.discountPrice = discountPrice;
        this.startDate = startDate;
        this.expirationDate = expirationDate;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
