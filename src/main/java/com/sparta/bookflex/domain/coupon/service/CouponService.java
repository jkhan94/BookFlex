package com.sparta.bookflex.domain.coupon.service;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.domain.coupon.dto.CouponRequestDto;
import com.sparta.bookflex.domain.coupon.dto.CouponResponseDto;
import com.sparta.bookflex.domain.coupon.dto.CouponUpdateRequestDto;
import com.sparta.bookflex.domain.coupon.dto.UserCouponResponseDto;
import com.sparta.bookflex.domain.coupon.entity.Coupon;
import com.sparta.bookflex.domain.coupon.entity.UserCoupon;
import com.sparta.bookflex.domain.coupon.enums.CouponStatus;
import com.sparta.bookflex.domain.coupon.repository.CouponRepository;
import com.sparta.bookflex.domain.coupon.repository.UserCouponRepository;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.enums.RoleType;
import com.sparta.bookflex.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

import static com.sparta.bookflex.common.exception.ErrorCode.*;
import static com.sparta.bookflex.domain.coupon.entity.Coupon.toCouponResponseDto;
import static com.sparta.bookflex.domain.coupon.entity.UserCoupon.toUserCouponEntity;
import static com.sparta.bookflex.domain.coupon.entity.UserCoupon.toUserCouponResponseDto;

@Service
@RequiredArgsConstructor
public class CouponService {
    private static final int PAGE_SIZE = 5;

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final AuthService authService;

    @Transactional
    public CouponResponseDto createCoupon(CouponRequestDto requestDto) {
        LocalDateTime now = LocalDateTime.now();
        CouponStatus status = CouponStatus.NotAvailable;

        if (now.isAfter(requestDto.getStartDate())) {
            status = CouponStatus.Available;
        }

        Coupon coupon = Coupon.builder()
                .couponName(requestDto.getCouponName())
                .couponStatus(status)
                .totalCount(requestDto.getTotalCount())
                .minPrice(requestDto.getMinPrice())
                .discountPrice(requestDto.getDiscountPrice())
                .startDate(requestDto.getStartDate())
                .expirationDate(requestDto.getExpirationDate())
                .build();

        couponRepository.save(coupon);

        return toCouponResponseDto(coupon);
    }


    @Transactional(readOnly = true)
    public List<CouponResponseDto> getAllCoupons(int page, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

        Page<CouponResponseDto> couponPage = couponRepository.findAll(pageable).map(Coupon::toCouponResponseDto);
        return couponPage.getContent();
    }


    @Transactional
    public CouponResponseDto updateCouponCount(long couponId,
                                               @RequestBody @Valid CouponUpdateRequestDto requestDto) {
        Coupon coupon = findCouponById(couponId);

        coupon.updateCount(requestDto);
        couponRepository.save(coupon);

        return toCouponResponseDto(coupon);
    }


    @Transactional
    public void deleteCoupon(long couponId) {
        Coupon coupon = findCouponById(couponId);

        // 이미 발급된 쿠폰은 삭제 불가.
        List<UserCoupon> issuedCoupon = userCouponRepository.findByCouponId(couponId);
        if (!issuedCoupon.isEmpty()) {
            throw new BusinessException(COUPON_DELETE_NOT_ALLOWED);
        }

        couponRepository.delete(coupon);
    }

    @Transactional
    public void issueCouponToAll(long couponId) {
        Coupon coupon = findCouponById(couponId);

        // 쿠폰이 발급가능 상태인지
        if (coupon.getCouponStatus() == CouponStatus.NotAvailable) {
            throw new BusinessException(COUPON_CANNOT_BE_ISSUED);
        }

        // 쿠폰 잔여수량 1이상인지
        List<User> users = authService.findAll();
        if (coupon.getTotalCount() <= users.size()) {
            throw new BusinessException(COUPON_CANNOT_BE_ISSUED_TO_ALL);
        }

        for (User user : users) {
            if (user.getAuth() == RoleType.USER) {
                UserCoupon issuedCoupon = userCouponRepository.findByUserAndCoupon(user, coupon);
                if (issuedCoupon == null) {
                    String couponCode = RandomStringUtils.randomAlphanumeric(20);

                    coupon.decreaseTotalCount();
                    couponRepository.save(coupon);

                    UserCoupon userCoupon = toUserCouponEntity(couponCode, false, null, user, coupon);
                    userCouponRepository.save(userCoupon);
                }
            }
        }
    }

    @Transactional
    public UserCouponResponseDto issueCouponToUser(long couponId, long userId) {
        Coupon coupon = findCouponById(couponId);

        if (coupon.getCouponStatus() == CouponStatus.NotAvailable) {
            throw new BusinessException(COUPON_CANNOT_BE_ISSUED);
        }

        if (coupon.getTotalCount() == 0) {
            throw new BusinessException(COUPON_CANNOT_BE_ISSUED);
        }

        // 유저 권한이 USER인지
        User user = authService.findById(userId);
        if (user.getAuth() != RoleType.USER) {
            throw new BusinessException(COUPON_ISSUE_NOT_ALLOWED);
        }

        // 유저가 이미 쿠폰을 받았는지.
        UserCoupon issuedCoupon = userCouponRepository.findByUserAndCoupon(user, coupon);
        if (issuedCoupon != null) {
            throw new BusinessException(COUPON_ALREADY_ISSUED);
        }

        String couponCode = RandomStringUtils.randomAlphanumeric(20);
        coupon.decreaseTotalCount();

        UserCoupon userCoupon = toUserCouponEntity(couponCode, false, null, user, coupon);

        couponRepository.save(coupon);
        userCouponRepository.save(userCoupon);

        return toUserCouponResponseDto(userCoupon, toCouponResponseDto(coupon));
    }

    @Transactional
    public UserCouponResponseDto issueCoupon(long couponId, User user) {
        Coupon coupon = findCouponById(couponId);

        if (coupon.getCouponStatus() == CouponStatus.NotAvailable) {
            throw new BusinessException(COUPON_CANNOT_BE_ISSUED);
        }

        if (coupon.getTotalCount() == 0) {
            throw new BusinessException(COUPON_CANNOT_BE_ISSUED);
        }

        UserCoupon issuedCoupon = userCouponRepository.findByUserAndCoupon(user, coupon);
        if (issuedCoupon != null) {
            throw new BusinessException(COUPON_ALREADY_ISSUED);
        }

        String couponCode = RandomStringUtils.randomAlphanumeric(20);
        coupon.decreaseTotalCount();

        UserCoupon userCoupon = toUserCouponEntity(couponCode, false, null, user, coupon);

        couponRepository.save(coupon);
        userCouponRepository.save(userCoupon);

        return toUserCouponResponseDto(userCoupon, toCouponResponseDto(coupon));
    }

    @Transactional(readOnly = true)
    public List<UserCouponResponseDto> getMyCoupons(User user, int page, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

        Page<UserCouponResponseDto> couponPage = userCouponRepository.findAllByUserId(user.getId(), pageable).map(
                userCoupon -> toUserCouponResponseDto(userCoupon, toCouponResponseDto(userCoupon.getCoupon()))
        );
        return couponPage.getContent();
    }

    @Transactional(readOnly = true)
    public UserCouponResponseDto getSingleUserCoupon(long couponId, User user) {
        Coupon coupon = findCouponById(couponId);

        UserCoupon userCoupon = userCouponRepository.findByUserAndCoupon(user, coupon);
        if (userCoupon == null) {
            throw new BusinessException(COUPON_NOT_ISSUED);
        }

        if (userCoupon.getIsUsed()) {
            throw new BusinessException(COUPON_ALREADY_USED);
        }

        return toUserCouponResponseDto(userCoupon, toCouponResponseDto(userCoupon.getCoupon()));
    }

    private Coupon findCouponById(long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new BusinessException(COUPON_NOT_FOUND)
        );
        return coupon;
    }

    // 쿠폰 선택 후 결제 시 쿠폰 상태 변경하는 메소드 필요.
    public void useCoupon(long couponId, User user) {
        Coupon coupon = findCouponById(couponId);

        UserCoupon issuedCoupon = userCouponRepository.findByUserAndCoupon(user, coupon);
        issuedCoupon.updateStatus();
    }

}
