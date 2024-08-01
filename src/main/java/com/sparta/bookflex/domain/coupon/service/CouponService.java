package com.sparta.bookflex.domain.coupon.service;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.domain.coupon.dto.*;
import com.sparta.bookflex.domain.coupon.entity.Coupon;
import com.sparta.bookflex.domain.coupon.entity.UserCoupon;
import com.sparta.bookflex.domain.coupon.enums.CouponStatus;
import com.sparta.bookflex.domain.coupon.repository.CouponRepository;
import com.sparta.bookflex.domain.coupon.repository.UserCouponRepository;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.enums.RoleType;
import com.sparta.bookflex.domain.user.enums.UserGrade;
import com.sparta.bookflex.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        CouponStatus status = CouponStatus.NOTAVAILABLE;

        if (now.isAfter(requestDto.getStartDate().atStartOfDay())) {
            status = CouponStatus.AVAILABLE;
        }

        Coupon coupon = Coupon.builder()
                .couponType(requestDto.getCouponType())
                .couponName(requestDto.getCouponName())
                .validityDays(requestDto.getValidityDays())
                .totalCount(requestDto.getTotalCount())
                .discountType(requestDto.getDiscountType())
                .minPrice(requestDto.getMinPrice())
                .discountPrice(requestDto.getDiscountPrice())
                .eligibleGrade(requestDto.getEligibleGrade())
                .couponStatus(status)
                .startDate(requestDto.getStartDate().atStartOfDay())
                .expirationDate(requestDto.getExpirationDate().atTime(23, 59, 59))
                .build();

        couponRepository.save(coupon);

        return toCouponResponseDto(coupon);
    }


    @Transactional(readOnly = true)
    public Page<CouponResponseDto> getAllCoupons(int page, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

        return couponRepository.findAll(pageable).map(Coupon::toCouponResponseDto);
    }


    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CouponResponseDto updateCouponCount(long couponId,
                                               @RequestBody @Valid CouponUpdateRequestDto requestDto) {
        Coupon coupon = findCouponByIdWithPessimisticLock(couponId);

        coupon.updateCount(requestDto);
        couponRepository.save(coupon);

        return toCouponResponseDto(coupon);
    }


    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteCoupon(long couponId) {
        Coupon coupon = findCouponByIdWithPessimisticLock(couponId);

        // 이미 발급된 쿠폰은 삭제 불가.
        List<UserCoupon> issuedCoupon = userCouponRepository.findByCouponId(couponId);
        if (!issuedCoupon.isEmpty()) {
            throw new BusinessException(COUPON_DELETE_NOT_ALLOWED);
        }

        couponRepository.delete(coupon);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void issueCouponToAll(long couponId) {
        Coupon coupon = findCouponByIdWithPessimisticLock(couponId);

        // 쿠폰이 발급가능 상태인지
        if (coupon.getCouponStatus() == CouponStatus.NOTAVAILABLE) {
            throw new BusinessException(COUPON_CANNOT_BE_ISSUED);
        }

        if (coupon.getEligibleGrade() == UserGrade.VIP) {
            // 쿠폰 잔여수량 회원 수 이상인지
            List<User> vipUsers = authService.findAll().stream()
                    .filter(user -> user.getAuth() == RoleType.USER)
                    .filter(user -> user.getGrade() == UserGrade.VIP)
                    .toList();
            if (coupon.getTotalCount() < vipUsers.size()) {
                throw new BusinessException(COUPON_CANNOT_BE_ISSUED_TO_ALL);
            }

            saveCoupons(vipUsers, coupon);

        } else if (coupon.getEligibleGrade() == UserGrade.NORMAL) {
            List<User> users = authService.findAll().stream()
                    .filter(user -> user.getAuth() == RoleType.USER)
                    .filter(user -> user.getGrade() == UserGrade.NORMAL)
                    .toList();
            if (coupon.getTotalCount() < users.size()) {
                throw new BusinessException(COUPON_CANNOT_BE_ISSUED_TO_ALL);
            }

            saveCoupons(users, coupon);

        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserCouponResponseDto issueCouponToUser(long couponId, long userId) {
        Coupon coupon = findCouponByIdWithPessimisticLock(couponId);

        // 쿠폰이 발급가능 상태인지
        if (coupon.getCouponStatus() == CouponStatus.NOTAVAILABLE) {
            throw new BusinessException(COUPON_CANNOT_BE_ISSUED);
        }

        // 쿠폰 잔여수량이 남아있는지
        if (coupon.getTotalCount() == 0) {
            throw new BusinessException(COUPON_CANNOT_BE_ISSUED);
        }

        // 사용자의 회원 등급으로 발급받을 수 있는 쿠폰인지
        User user = authService.findById(userId);
        if (user.getGrade() != coupon.getEligibleGrade()) {
            throw new BusinessException(COUPON_CANNOT_BE_ISSUED);
        }

        // 유저가 이미 쿠폰을 받았는지.
        UserCoupon issuedCoupon = userCouponRepository.findByUserAndCoupon(user, coupon);
        if (issuedCoupon != null) {
            throw new BusinessException(COUPON_ALREADY_ISSUED);
        }

        String couponCode = RandomStringUtils.randomAlphanumeric(20);
        coupon.decreaseTotalCount();

        LocalDateTime issuedAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime expirationDate;
        if (coupon.getValidityDays() == 0) {
            expirationDate = coupon.getExpirationDate();
        } else {
            expirationDate = issuedAt.plusDays(coupon.getValidityDays());
        }

        UserCoupon userCoupon = toUserCouponEntity(couponCode, issuedAt, expirationDate, false, null, user, coupon);

        couponRepository.save(coupon);
        userCouponRepository.save(userCoupon);

        return toUserCouponResponseDto(userCoupon, toCouponResponseDto(coupon));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserCouponResponseDto issueCoupon(long couponId, User user) {
        Coupon coupon = findCouponByIdWithPessimisticLock(couponId);

        if (coupon.getCouponStatus() == CouponStatus.NOTAVAILABLE) {
            throw new BusinessException(COUPON_CANNOT_BE_ISSUED);
        }

        if (coupon.getTotalCount() == 0) {
            throw new BusinessException(COUPON_CANNOT_BE_ISSUED);
        }

        if (user.getGrade() != coupon.getEligibleGrade()) {
            throw new BusinessException(COUPON_CANNOT_BE_ISSUED);
        }

        UserCoupon issuedCoupon = userCouponRepository.findByUserAndCoupon(user, coupon);
        if (issuedCoupon != null) {
            throw new BusinessException(COUPON_ALREADY_ISSUED);
        }

        String couponCode = RandomStringUtils.randomAlphanumeric(20);
        coupon.decreaseTotalCount();

        LocalDateTime issuedAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime expirationDate;
        if (coupon.getValidityDays() == 0) {
            expirationDate = coupon.getExpirationDate();
        } else {
            expirationDate = issuedAt.plusDays(coupon.getValidityDays());
        }

        UserCoupon userCoupon = toUserCouponEntity(couponCode, issuedAt, expirationDate, false, null, user, coupon);

        couponRepository.save(coupon);
        userCouponRepository.save(userCoupon);

        return toUserCouponResponseDto(userCoupon, toCouponResponseDto(coupon));
    }

    @Transactional(readOnly = true)
    public Page<UserCouponResponseDto> getMyCoupons(User user, int page, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

        return userCouponRepository.findAllByUserId(user.getId(), pageable).map(
                userCoupon -> toUserCouponResponseDto(userCoupon, toCouponResponseDto(userCoupon.getCoupon()))
        );
    }

    @Transactional(readOnly = true)
    public Page<CouponResponseDto> getAvailableCoupons(User user, int page, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

        List<Long> issuedCouponIds = userCouponRepository.findAllByUserId(user.getId())
                .stream()
                .map(uc -> uc.getCoupon().getId())
                .toList();

        return couponRepository.findAvailableByUserGrade(user, pageable, issuedCouponIds).map(Coupon::toCouponResponseDto);
    }

    @Transactional(readOnly = true)
    public UserCouponResponseDto getSingleUserCoupon(long couponId, User user) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new BusinessException(COUPON_NOT_FOUND)
        );

        UserCoupon userCoupon = userCouponRepository.findByUserAndCoupon(user, coupon);
        if (userCoupon == null) {
            throw new BusinessException(COUPON_NOT_ISSUED);
        }

        if (userCoupon.getIsUsed()) {
            throw new BusinessException(COUPON_ALREADY_USED);
        }

        return toUserCouponResponseDto(userCoupon, toCouponResponseDto(userCoupon.getCoupon()));
    }

    public Coupon findCouponByIdWithPessimisticLock(long couponId) {
        Coupon coupon = couponRepository.findByIdWithPessimisticLock(couponId).orElseThrow(
                () -> new BusinessException(COUPON_NOT_FOUND)
        );
        return coupon;
    }

    private void saveCoupons(List<User> userList, Coupon coupon) {
        LocalDateTime issuedAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime expirationDate;

        for (User user : userList) {
            UserCoupon issuedCoupon = userCouponRepository.findByUserAndCoupon(user, coupon);
            if (issuedCoupon == null) {
                String couponCode = RandomStringUtils.randomAlphanumeric(20);

                coupon.decreaseTotalCount();

                if (coupon.getValidityDays() == 0) {
                    expirationDate = coupon.getExpirationDate();
                } else {
                    expirationDate = issuedAt.plusDays(coupon.getValidityDays());
                }

                UserCoupon userCoupon = toUserCouponEntity(couponCode, issuedAt, expirationDate, false, null, user, coupon);

                couponRepository.save(coupon);
                userCouponRepository.save(userCoupon);
            }
        }
    }

    @Transactional
    public List<CouponOrderResponseDto> getMyOrderCoupons(User user, Pageable pageable) {


        Page<UserCoupon> userCouponPage = userCouponRepository.findAllByUserId(user.getId(), pageable);

        Page<CouponOrderResponseDto> couponOrderResponseDtoPage = new PageImpl<>(
                userCouponPage.getContent().stream()
                        .filter(userCoupon -> !userCoupon.getIsUsed())
                        .map(userCoupon -> {
                            Coupon coupon = userCoupon.getCoupon();
                            if (coupon == null) {
                                throw new BusinessException(COUPON_NOT_FOUND);
                            }
                            return CouponOrderResponseDto.builder()
                                    .couponName(coupon.getCouponName())
                                    .userCouponId(userCoupon.getId())
                                    .couponCode(userCoupon.getCouponCode())
                                    .discountType(coupon.getDiscountType())
                                    .minPrice(coupon.getMinPrice())
                                    .discountPrice(coupon.getDiscountPrice())
                                    .build();
                        })
                        .collect(Collectors.toList()),
                userCouponPage.getPageable(),
                userCouponPage.getTotalElements()
        );

        return couponOrderResponseDtoPage.getContent();
    }

    public UserCoupon findUserCouponById(long userCouponId) {
        return userCouponRepository.findById(userCouponId).orElseThrow(
                () -> new BusinessException(COUPON_NOT_FOUND)
        );
    }
}
