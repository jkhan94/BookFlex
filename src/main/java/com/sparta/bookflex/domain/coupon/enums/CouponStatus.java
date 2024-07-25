package com.sparta.bookflex.domain.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponStatus {
    NotAvailable(CouponStatusCode.NotAvailable),
    Available(CouponStatusCode.Available);

    private final String status;

    private static class CouponStatusCode {
        public static final String NotAvailable = "사용불가";
        public static final String Available = "사용가능";
    }
}
