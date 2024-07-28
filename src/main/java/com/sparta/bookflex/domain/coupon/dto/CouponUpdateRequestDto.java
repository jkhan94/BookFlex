package com.sparta.bookflex.domain.coupon.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponUpdateRequestDto {

    @NotNull(message = "추가 발급할 쿠폰 개수를 입력해주세요.")
    @Min(value = 1, message = "쿠폰은 1개 이상부터 발급할 수 있습니다.")
    private int totalCount;
}
