package com.sparta.bookflex.domain.coupon.repository;

import com.sparta.bookflex.domain.coupon.entity.Coupon;
import com.sparta.bookflex.domain.coupon.entity.UserCoupon;
import com.sparta.bookflex.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long>, UserCouponQRepository {
    List<UserCoupon> findByCouponId(long couponId);

    UserCoupon findByUserAndCoupon(User user, Coupon coupon);

    Page<UserCoupon> findByUserIdAndIsUsedFalse(long id, Pageable pageable);
}
