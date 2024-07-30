package com.sparta.bookflex.domain.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.bookflex.common.aop.ValidEnum;
import com.sparta.bookflex.domain.coupon.enums.CouponType;
import com.sparta.bookflex.domain.coupon.enums.DiscountType;
import com.sparta.bookflex.domain.user.enums.UserGrade;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponRequestDto {

    @ValidEnum(enumClass = CouponType.class, message = "쿠폰 종류를 선택해주세요.")
    private CouponType couponType;

    @NotBlank(message = "쿠폰 이름을 입력해주세요.")
    private String couponName;

    @Min(value = 0, message = "발급일로부터 n일간 유효한 쿠폰을 만들려면 자연수 n을 입력하세요. 쿠폰 발급 기간 동안 사용 가능한 쿠폰을 만들려면 0을 입력하세요.")
    private int validityDays;

    @NotNull(message = "쿠폰을 발급할 총 개수를 입력해주세요.")
    @Min(value = 1, message = "쿠폰은 1개 이상부터 발급할 수 있습니다.")
    private int totalCount;

    @ValidEnum(enumClass = DiscountType.class, message = "할인 유형을 선택해주세요.")
    private DiscountType discountType;

    @NotNull(message = "쿠폰을 사용할 수 있는 최소 주문 금액을 입력해주세요.")
    @Min(value = 0, message = "최소주문금액은 0원 이상입니다.")
    private BigDecimal minPrice;

    @NotNull(message = "할인 금액이나 비율을 입력해주세요.")
    @Min(value = 0, message = "할인 금액은 최소 0원 이상, 할인 비율은 최소 0 % 이상입니다.")
    private BigDecimal discountPrice;

    @ValidEnum(enumClass = UserGrade.class, message = "쿠폰을 발급할 수 있는 회원 등급을 입력해주세요.")
    private UserGrade eligibleGrade;

    @NotNull(message = "쿠폰 사용 시작일을 입력해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "쿠폰 만료일을 입력해주세요.")
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;
}
