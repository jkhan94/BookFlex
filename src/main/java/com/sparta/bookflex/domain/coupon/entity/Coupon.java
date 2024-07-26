package com.sparta.bookflex.domain.coupon.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


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
    private String couponCode;

//    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
//    private CouponStatus couponStatus;

    @Column(nullable = false)
    private int totalCount;

    @Column(nullable = false, precision = 12, scale=2)
    private BigDecimal minPrice;

    @Column(nullable = false, precision = 12, scale=2)
    private BigDecimal discountPrice;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @OneToMany(mappedBy="coupon", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserCoupon> userCouponList;

}
