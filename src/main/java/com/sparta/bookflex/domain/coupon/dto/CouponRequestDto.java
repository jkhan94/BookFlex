package com.sparta.bookflex.domain.coupon.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponRequestDto {

    @NotBlank(message = "쿠폰 이름을 입력해주세요.")
    private String couponName;

    @NotNull(message = "쿠폰을 발급할 총 개수를 입력해주세요.")
    @Min(value=1, message="쿠폰은 1개 이상부터 발급할 수 있습니다.")
    private int totalCount;

    @NotNull(message = "쿠폰을 사용할 수 있는 최소 주문 금액을 입력해주세요.")
    @Min(value=0, message="최소주문금액은 0원 이상입니다.")
    private BigDecimal minPrice;

    @NotNull(message = "할인 금액을 입력해주세요.")
    @Min(value=0, message="할인 금액은 최소 0원 이상입니다.")
    private BigDecimal discountPrice;

    @NotNull(message = "쿠폰 사용 시작일을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @NotNull(message = "쿠폰 만료일을 입력해주세요.")
    @FutureOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expirationDate;
}
