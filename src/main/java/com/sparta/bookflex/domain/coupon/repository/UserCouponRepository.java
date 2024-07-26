package com.sparta.bookflex.domain.coupon.repository;

import com.sparta.bookflex.domain.coupon.entity.Coupon;
import com.sparta.bookflex.domain.coupon.entity.UserCoupon;
import com.sparta.bookflex.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long>, UserCouponQRepository {
    UserCoupon findByCouponId(long couponId);

    UserCoupon findByUserAndCoupon(User user, Coupon coupon);
}
