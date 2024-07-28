package com.sparta.bookflex.domain.coupon.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.coupon.dto.CouponResponseDto;
import com.sparta.bookflex.domain.coupon.dto.CouponUpdateRequestDto;
import com.sparta.bookflex.domain.coupon.enums.CouponStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String couponName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;

    @Column(nullable = false)
    private int totalCount;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal minPrice;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal discountPrice;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

//    @OneToMany(mappedBy="coupon", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private List<UserCoupon> userCouponList;

    @Builder
    public Coupon(String couponName, CouponStatus couponStatus, int totalCount, BigDecimal minPrice, BigDecimal discountPrice, LocalDateTime startDate, LocalDateTime expirationDate) {
        this.couponName = couponName;
        this.couponStatus = couponStatus;
        this.totalCount = totalCount;
        this.minPrice = minPrice;
        this.discountPrice = discountPrice;
        this.startDate = startDate;
        this.expirationDate = expirationDate;
    }

    public static CouponResponseDto toCouponResponseDto(Coupon coupon) {
        return CouponResponseDto.builder()
                .couponId(coupon.getId())
                .couponName(coupon.getCouponName())
                .couponStatus(coupon.getCouponStatus())
                .totalCount(coupon.getTotalCount())
                .minPrice(coupon.getMinPrice())
                .discountPrice(coupon.getDiscountPrice())
                .startDate(coupon.getStartDate())
                .expirationDate(coupon.getExpirationDate())
                .createdAt(coupon.getCreatedAt())
                .modifiedAt(coupon.getModifiedAt())
                .build();
    }

    public void updateCount(CouponUpdateRequestDto requestDto) {
        this.totalCount = requestDto.getTotalCount();
    }

    public void decreaseTotalCount() {
        this.totalCount--;
    }

}
