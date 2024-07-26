package com.sparta.bookflex.domain.coupon.service;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.domain.coupon.dto.CouponRequestDto;
import com.sparta.bookflex.domain.coupon.dto.CouponResponseDto;
import com.sparta.bookflex.domain.coupon.dto.CouponStatusRequestDto;
import com.sparta.bookflex.domain.coupon.entity.Coupon;
import com.sparta.bookflex.domain.coupon.entity.UserCoupon;
import com.sparta.bookflex.domain.coupon.enums.CouponStatus;
import com.sparta.bookflex.domain.coupon.repository.CouponRepository;
import com.sparta.bookflex.domain.coupon.repository.UserCouponRepository;
import com.sparta.bookflex.domain.user.entity.User;
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
import java.time.ZoneId;
import java.util.List;

import static com.sparta.bookflex.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CouponService {
    private static final int PAGE_SIZE = 5;

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

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

        return CouponResponseDto.builder()
                .couponId(coupon.getId())
                .couponName(coupon.getCouponName())
                .couponStatus(coupon.getCouponStatus())
                .totalCount(coupon.getTotalCount())
                .minPrice(coupon.getMinPrice())
                .discountPrice(coupon.getDiscountPrice())
                .startDate(coupon.getStartDate())
                .expirationDate(coupon.getExpirationDate())
                .createdAt(coupon.getCreatedAt())
                .modifiedAt(coupon.getModifiedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<CouponResponseDto> getMyCoupons(User user, int page, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

        Page<CouponResponseDto> couponPage = couponRepository.findAllByUserId(user.getId(), pageable).map(
                coupon -> CouponResponseDto.builder()
                        .couponId(coupon.getId())
                        .couponName(coupon.getCouponName())
                        .couponStatus(coupon.getCouponStatus())
                        .totalCount(coupon.getTotalCount())
                        .minPrice(coupon.getMinPrice())
                        .discountPrice(coupon.getDiscountPrice())
                        .startDate(coupon.getStartDate())
                        .expirationDate(coupon.getExpirationDate())
                        .createdAt(coupon.getCreatedAt())
                        .modifiedAt(coupon.getModifiedAt())
                        .build()
        );
        return couponPage.getContent();
    }

    @Transactional(readOnly = true)
    public List<CouponResponseDto> getAllCoupons(int page, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

        Page<CouponResponseDto> couponPage = couponRepository.findAll(pageable).map(
                coupon -> CouponResponseDto.builder()
                        .couponId(coupon.getId())
                        .couponName(coupon.getCouponName())
                        .couponStatus(coupon.getCouponStatus())
                        .totalCount(coupon.getTotalCount())
                        .minPrice(coupon.getMinPrice())
                        .discountPrice(coupon.getDiscountPrice())
                        .startDate(coupon.getStartDate())
                        .expirationDate(coupon.getExpirationDate())
                        .createdAt(coupon.getCreatedAt())
                        .modifiedAt(coupon.getModifiedAt())
                        .build()
        );
        return couponPage.getContent();
    }

    @Transactional
    public CouponResponseDto updateCouponStatus(long couponId,
                                                @RequestBody @Valid CouponStatusRequestDto requestDto) {
        //    쿠폰이 있는지
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new BusinessException(COUPON_NOT_FOUND)
        );

        coupon.updateStatus(requestDto);
        couponRepository.save(coupon);

        return CouponResponseDto.builder()
                .couponId(coupon.getId())
                .couponName(coupon.getCouponName())
                .couponStatus(coupon.getCouponStatus())
                .totalCount(coupon.getTotalCount())
                .minPrice(coupon.getMinPrice())
                .discountPrice(coupon.getDiscountPrice())
                .startDate(coupon.getStartDate())
                .expirationDate(coupon.getExpirationDate())
                .createdAt(coupon.getCreatedAt())
                .modifiedAt(coupon.getModifiedAt())
                .build();
    }

    @Transactional
    public void deleteCoupon(long couponId) {
        //    쿠폰이 있는지
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new BusinessException(COUPON_NOT_FOUND)
        );

        //    이미 발급된 쿠폰은 삭제 불가.
        UserCoupon issuedCoupon = userCouponRepository.findByCouponId(couponId);
        if (issuedCoupon != null) {
            throw new BusinessException(COUPON_DELETE_NOT_ALLOWED);
        }

        couponRepository.delete(coupon);
    }

    @Transactional
    public CouponResponseDto issueCoupon(long couponId, User user) {

        // 쿠폰이 있는지
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new BusinessException(COUPON_NOT_FOUND)
        );

        //   쿠폰 잔여수량 1이상인지
        if (coupon.getTotalCount() < 0) {
            throw new BusinessException(COUPON_ISSUE_NOT_ALLOWED);
        }

        //    오늘 날짜가 startDate와 expirationDate사이인지.(사용기간)
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        if (now.isBefore(coupon.getStartDate()) || now.isAfter(coupon.getExpirationDate())) {
            throw new BusinessException(COUPON_ISSUE_NOT_ALLOWED);
        }

        //    유저가 이미 쿠폰을 받았는지.
        UserCoupon issuedCoupon = userCouponRepository.findByUserAndCoupon(user, coupon);
        if (issuedCoupon != null) {
            throw new BusinessException(COUPON_ALREADY_ISSUED);
        }

        String couponCode = RandomStringUtils.randomAlphanumeric(20);
        coupon.decreaseTotalCount();

        UserCoupon userCoupon = UserCoupon.builder()
                .couponCode(couponCode)
                .isUsed(false)
                .usedAt(null)
                .user(user)
                .coupon(coupon)
                .build();

        couponRepository.save(coupon);
        userCouponRepository.save(userCoupon);

        return CouponResponseDto.builder()
                .couponId(coupon.getId())
                .couponName(coupon.getCouponName())
                .couponStatus(coupon.getCouponStatus())
                .totalCount(coupon.getTotalCount())
                .minPrice(coupon.getMinPrice())
                .discountPrice(coupon.getDiscountPrice())
                .startDate(coupon.getStartDate())
                .expirationDate(coupon.getExpirationDate())
                .createdAt(coupon.getCreatedAt())
                .modifiedAt(coupon.getModifiedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public CouponResponseDto getSingleUserCoupon(long couponId, User user) {
        //    쿠폰이 있는지
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new BusinessException(COUPON_NOT_FOUND)
        );

        // 발급받은 쿠폰인지
        UserCoupon issuedCoupon = userCouponRepository.findByUserAndCoupon(user, coupon);
        if (issuedCoupon == null) {
            throw new BusinessException(COUPON_NOT_ISSUED);
        }

        // 쿠폰이 미사용인지
        if (issuedCoupon.getIsUsed()) {
            throw new BusinessException(COUPON_ALREADY_USED);
        }

        return CouponResponseDto.builder()
                .couponId(coupon.getId())
                .couponName(coupon.getCouponName())
                .couponStatus(coupon.getCouponStatus())
                .totalCount(coupon.getTotalCount())
                .minPrice(coupon.getMinPrice())
                .discountPrice(coupon.getDiscountPrice())
                .startDate(coupon.getStartDate())
                .expirationDate(coupon.getExpirationDate())
                .createdAt(coupon.getCreatedAt())
                .modifiedAt(coupon.getModifiedAt())
                .build();
    }


    // 쿠폰 선택 후 결제 시 쿠폰 상태 변경하는 메소드 필요.
    public void useCoupon(long couponId, User user) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new BusinessException(COUPON_NOT_FOUND)
        );

        UserCoupon issuedCoupon = userCouponRepository.findByUserAndCoupon(user, coupon);
        issuedCoupon.updateStatus();
    }
}
