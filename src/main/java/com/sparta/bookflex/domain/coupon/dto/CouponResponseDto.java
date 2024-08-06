package com.sparta.bookflex.domain.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.bookflex.domain.coupon.enums.CouponStatus;
import com.sparta.bookflex.domain.coupon.enums.CouponType;
import com.sparta.bookflex.domain.coupon.enums.DiscountType;
import com.sparta.bookflex.domain.user.enums.UserGrade;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CouponResponseDto {
    private Long couponId;
    private CouponType couponType;
    private String couponName;
    private int validityDays;
    private int totalCount;
    private DiscountType discountType;
    private BigDecimal minPrice;
    private BigDecimal discountPrice;
    private UserGrade eligibleGrade;
    private CouponStatus couponStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime expirationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    @Builder
    public CouponResponseDto(Long couponId, CouponType couponType, String couponName, int validityDays, int totalCount,
                             DiscountType discountType, BigDecimal minPrice, BigDecimal discountPrice, UserGrade eligibleGrade,
                             CouponStatus couponStatus, LocalDateTime startDate, LocalDateTime expirationDate,
                             LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.couponId = couponId;
        this.couponType = couponType;
        this.couponName = couponName;
        this.validityDays = validityDays;
        this.totalCount = totalCount;
        this.discountType = discountType;
        this.minPrice = minPrice;
        this.discountPrice = discountPrice;
        this.eligibleGrade = eligibleGrade;
        this.couponStatus = couponStatus;
        this.startDate = startDate;
        this.expirationDate = expirationDate;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
