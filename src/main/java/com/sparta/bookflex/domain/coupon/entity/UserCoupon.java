package com.sparta.bookflex.domain.coupon.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueCouponAndUser", columnNames = {"user_id", "coupon_id"})})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Column(nullable = false)
    private Boolean isUsed;

    @Column
    private LocalDateTime usedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

}


