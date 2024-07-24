package com.sparta.bookflex.domain.usercoupon.repository;

import com.sparta.bookflex.domain.usercoupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
}
