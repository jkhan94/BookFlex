package com.sparta.bookflex.domain.coupon.controller;

import com.sparta.bookflex.common.aop.Envelop;
import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.coupon.dto.CouponRequestDto;
import com.sparta.bookflex.domain.coupon.dto.CouponResponseDto;
import com.sparta.bookflex.domain.coupon.dto.CouponUpdateRequestDto;
import com.sparta.bookflex.domain.coupon.dto.UserCouponResponseDto;
import com.sparta.bookflex.domain.coupon.enums.CouponType;
import com.sparta.bookflex.domain.coupon.enums.DiscountType;
import com.sparta.bookflex.domain.coupon.service.CouponService;
import com.sparta.bookflex.domain.qna.enums.QnaTypeCode;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.enums.RoleType;
import com.sparta.bookflex.domain.user.enums.UserGrade;
import com.sparta.bookflex.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.bookflex.common.exception.ErrorCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;
    private final AuthService authService;

    @PostMapping
    @Envelop("쿠폰을 생성했습니다.")
    public ResponseEntity<CouponResponseDto> createCoupon(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @RequestBody @Valid CouponRequestDto requestDto) {
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.ADMIN) {
            throw new BusinessException(COUPON_CREATE_NOT_ALLOWED);
        }

        if (requestDto.getStartDate().isAfter(requestDto.getExpirationDate())) {
            throw new BusinessException(COUPON_CREATE_NOT_ALLOWED);
        }

        CouponResponseDto responseDto = couponService.createCoupon(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/admin")
    @Envelop("발급 가능한 모든 쿠폰을 조회했습니다.")
    public ResponseEntity<List<CouponResponseDto>> getAllCoupons(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                                                 @RequestParam(value = "sortBy", defaultValue = "expirationDate") String sortBy) {
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.ADMIN) {
            throw new BusinessException(COUPON_VIEW_NOT_ALLOWED);
        }

        List<CouponResponseDto> responseList = couponService.getAllCoupons(page - 1, sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }


    @PutMapping("/{couponId}")
    @Envelop("쿠폰 개수를 변경했습니다.")
    public ResponseEntity<CouponResponseDto> updateCouponCount(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @RequestBody @Valid CouponUpdateRequestDto requestDto,
                                                               @PathVariable long couponId) {
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.ADMIN) {
            throw new BusinessException(COUPON_UPDATE_NOT_ALLOWED);
        }

        CouponResponseDto responseDto = couponService.updateCouponCount(couponId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @DeleteMapping("/{couponId}")
    @Envelop("쿠폰을 삭제했습니다.")
    public ResponseEntity<?> deleteCoupon(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @PathVariable long couponId) {
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.ADMIN) {
            throw new BusinessException(COUPON_DELETE_NOT_ALLOWED);
        }

        couponService.deleteCoupon(couponId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/issue/all/{couponId}")
    @Envelop("쿠폰이 일괄 발급되었습니다.")
    public ResponseEntity<?> issueCouponToAll(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @PathVariable long couponId) {
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.ADMIN) {
            throw new BusinessException(COUPON_ISSUE_NOT_ALLOWED);
        }

        couponService.issueCouponToAll(couponId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/issue/{userId}/{couponId}")
    @Envelop("사용자에게 쿠폰이 발급되었습니다.")
    public ResponseEntity<UserCouponResponseDto> issueCouponToUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                   @PathVariable long userId,
                                                                   @PathVariable long couponId) {
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.ADMIN) {
            throw new BusinessException(COUPON_ISSUE_NOT_ALLOWED);
        }

        UserCouponResponseDto responseDto = couponService.issueCouponToUser(couponId, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @PostMapping("/issue/{couponId}")
    @Envelop("쿠폰이 발급되었습니다.")
    public ResponseEntity<UserCouponResponseDto> issueCoupon(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @PathVariable long couponId) {
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.USER) {
            throw new BusinessException(COUPON_ISSUE_NOT_ALLOWED);
        }

        UserCouponResponseDto responseDto = couponService.issueCoupon(couponId, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    @Envelop("발급받은 모든 쿠폰을 조회했습니다.")
    public ResponseEntity<List<UserCouponResponseDto>> getMyCoupons(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                                                    @RequestParam(value = "sortBy", defaultValue = "isUsed") String sortBy) {
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.USER) {
            throw new BusinessException(COUPON_VIEW_NOT_ALLOWED);
        }

        List<UserCouponResponseDto> responseList = couponService.getMyCoupons(user, page - 1, sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/availables")
    @Envelop("발급 가능한 모든 쿠폰을 조회했습니다.")
    public ResponseEntity<List<CouponResponseDto>> getAvailableCoupons(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                                                    @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy) {
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.USER) {
            throw new BusinessException(COUPON_VIEW_NOT_ALLOWED);
        }

        List<CouponResponseDto> responseList = couponService.getAvailableCoupons(user, page - 1, sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/use/{couponId}")
    @Envelop("사용할 쿠폰을 조회했습니다.")
    public ResponseEntity<UserCouponResponseDto> getSingleUserCoupon(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @PathVariable long couponId) {
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.USER) {
            throw new BusinessException(COUPON_VIEW_NOT_ALLOWED);
        }

        UserCouponResponseDto responseDto = couponService.getSingleUserCoupon(couponId, user);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/types")
    @Envelop("쿠폰 종류를 조회했습니다.")
    public ResponseEntity<List<String>> getCouponType() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Arrays.stream(CouponType.values())
                        .map(CouponType::getCouponType)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/discounts")
    @Envelop("할인 유형을 조회했습니다.")
    public ResponseEntity<List<String>> getDiscountType() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Arrays.stream(DiscountType.values())
                        .map(DiscountType::getDiscountType)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/grades")
    @Envelop("사용자 등급을 조회했습니다.")
    public ResponseEntity<List<String>> getUserGrade() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Arrays.stream(UserGrade.values())
                        .map(UserGrade::getUserGrade)
                        .collect(Collectors.toList()));
    }
}