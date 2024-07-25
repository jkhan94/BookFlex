package com.sparta.bookflex.domain.coupon.dto;

import com.sparta.bookflex.domain.coupon.enums.CouponStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponRequestDto {
    private String couponName;
    private int totalCount;
    private BigDecimal minPrice;
    private BigDecimal discountPrice;
    private LocalDateTime startDate;
    private LocalDateTime expirationDate;
}
