package com.sparta.bookflex.domain.coupon.dto;

import com.sparta.bookflex.common.aop.ValidEnum;
import com.sparta.bookflex.domain.coupon.enums.CouponStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponStatusRequestDto {

    @ValidEnum(enumClass = CouponStatus.class, message = "쿠폰 상태를 입력해주세요.")
    private CouponStatus couponStatus;
}
