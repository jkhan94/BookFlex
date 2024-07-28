package com.sparta.bookflex.domain.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime expirationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
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
