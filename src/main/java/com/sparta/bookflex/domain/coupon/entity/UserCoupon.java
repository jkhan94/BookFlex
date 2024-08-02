package com.sparta.bookflex.domain.coupon.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.coupon.dto.CouponResponseDto;
import com.sparta.bookflex.domain.coupon.dto.UserCouponResponseDto;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueCouponAndUser", columnNames = {"user_id", "coupon_id"})})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String couponCode;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private Boolean isUsed;

    @Column
    private LocalDateTime usedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Coupon coupon;

    @Builder
    public UserCoupon(String couponCode, LocalDateTime issuedAt, LocalDateTime expirationDate, Boolean isUsed, LocalDateTime usedAt, User user, Coupon coupon) {
        this.couponCode = couponCode;
        this.issuedAt = issuedAt;
        this.expirationDate = expirationDate;
        this.isUsed = isUsed;
        this.usedAt = usedAt;
        this.user = user;
        this.coupon = coupon;
    }

    public static UserCouponResponseDto toUserCouponResponseDto(UserCoupon userCoupon, CouponResponseDto responseDto) {
        return UserCouponResponseDto.builder()
                .userCouponId(userCoupon.getId())
                .couponCode(userCoupon.getCouponCode())
                .issuedAt(userCoupon.getIssuedAt())
                .expirationDate(userCoupon.getExpirationDate())
                .isUsed(userCoupon.getIsUsed())
                .usedAt(userCoupon.getUsedAt())
                .coupon(responseDto)
                .build();
    }

    public static UserCoupon toUserCouponEntity(String couponCode, LocalDateTime issuedAt, LocalDateTime expirationDate, Boolean isUsed, LocalDateTime usedAt, User user, Coupon coupon) {
        return UserCoupon.builder()
                .couponCode(couponCode)
                .issuedAt(issuedAt)
                .expirationDate(expirationDate)
                .isUsed(isUsed)
                .usedAt(usedAt)
                .user(user)
                .coupon(coupon)
                .build();
    }

    public void updateStatus() {
        this.isUsed = true;
        this.usedAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

}


