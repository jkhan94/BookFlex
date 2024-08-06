package com.sparta.bookflex.domain.coupon.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.coupon.dto.CouponResponseDto;
import com.sparta.bookflex.domain.coupon.dto.CouponUpdateRequestDto;
import com.sparta.bookflex.domain.coupon.enums.CouponStatus;
import com.sparta.bookflex.domain.coupon.enums.CouponType;
import com.sparta.bookflex.domain.coupon.enums.DiscountType;
import com.sparta.bookflex.domain.user.enums.UserGrade;
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
    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Column(nullable = false)
    private String couponName;

    @Column(nullable = true)
    private int validityDays;

    @Column(nullable = false)
    private int totalCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal minPrice;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal discountPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserGrade eligibleGrade;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

//    @OneToMany(mappedBy="coupon", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private List<UserCoupon> userCouponList;

    @Builder
    public Coupon(CouponType couponType, String couponName, int validityDays, int totalCount, DiscountType discountType, BigDecimal minPrice,
                  BigDecimal discountPrice, UserGrade eligibleGrade, CouponStatus couponStatus, LocalDateTime startDate,
                  LocalDateTime expirationDate) {
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
    }

    public static CouponResponseDto toCouponResponseDto(Coupon coupon) {
        return CouponResponseDto.builder()
                .couponId(coupon.getId())
                .couponType(coupon.getCouponType())
                .couponName(coupon.getCouponName())
                .validityDays(coupon.getValidityDays())
                .totalCount(coupon.getTotalCount())
                .discountType(coupon.getDiscountType())
                .minPrice(coupon.getMinPrice())
                .discountPrice(coupon.getDiscountPrice())
                .eligibleGrade(coupon.getEligibleGrade())
                .couponStatus(coupon.getCouponStatus())
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
