package com.sparta.bookflex.domain.coupon.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name="user_coupon")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@IdClass(UserCouponId.class)
public class UserCoupon extends Timestamped implements Serializable {


    @Column(nullable = false)
    private Boolean isUsed;

    @Column
    private LocalDateTime usedAt;

    @Id
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name="coupon_id")
    private Coupon coupon;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UserCoupon that = (UserCoupon) object;
        return Objects.equals(user.getId(), that.user.getId()) &&
                Objects.equals(coupon.getId(), that.coupon.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, coupon);
    }

}

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
class UserCouponId implements Serializable {
    private Long user;
    private Long coupon;

}

