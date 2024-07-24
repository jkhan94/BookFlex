package com.sparta.bookflex.domain.coupon.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.usercoupon.entity.UserCoupon;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Coupon extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String couponCode;

    @Column(nullable = false, precision = 12, scale=2)
    private BigDecimal discountAmount;

    @Column(nullable = false, precision = 12, scale=2)
    private BigDecimal discountPercentage;

    @Column(nullable = false)
    private LocalDateTime experationDate;

    @OneToMany(mappedBy="coupon", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserCoupon> userCouponList = new ArrayList<>();

}
