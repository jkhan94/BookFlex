package com.sparta.bookflex.domain.coupon.controller;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.coupon.dto.*;
import com.sparta.bookflex.domain.coupon.enums.CouponType;
import com.sparta.bookflex.domain.coupon.enums.DiscountType;
import com.sparta.bookflex.domain.coupon.service.CouponService;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.enums.RoleType;
import com.sparta.bookflex.domain.user.enums.UserGrade;
import com.sparta.bookflex.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<Page<CouponResponseDto>> getAllCoupons(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                                                 @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy) {
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.ADMIN) {
            throw new BusinessException(COUPON_VIEW_NOT_ALLOWED);
        }

        Page<CouponResponseDto> responseList = couponService.getAllCoupons(page - 1, sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @PutMapping("/{couponId}")
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
    public ResponseEntity<?> issueCouponToAll(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @PathVariable long couponId) {
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.ADMIN) {
            throw new BusinessException(COUPON_ISSUE_NOT_ALLOWED);
        }

        couponService.issueCouponToAll(couponId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/issue/{couponId}")
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
    public ResponseEntity<Page<UserCouponResponseDto>> getMyCoupons(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                                                    @RequestParam(value = "sortBy", defaultValue = "expirationDate") String sortBy) {
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.USER) {
            throw new BusinessException(COUPON_VIEW_NOT_ALLOWED);
        }

        Page<UserCouponResponseDto> responseList = couponService.getMyCoupons(user, page - 1, sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/availables")
    public ResponseEntity<Page<CouponResponseDto>> getAvailableCoupons(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                                                       @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy) {
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.USER) {
            throw new BusinessException(COUPON_VIEW_NOT_ALLOWED);
        }

        Page<CouponResponseDto> responsePage = couponService.getAvailableCoupons(user, page - 1, sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(responsePage);
    }

    @GetMapping("/use/{couponId}")
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
    public ResponseEntity<List<String>> getCouponType() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Arrays.stream(CouponType.values())
                        .map(CouponType::getCouponType)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/discounts")
    public ResponseEntity<List<String>> getDiscountType() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Arrays.stream(DiscountType.values())
                        .map(DiscountType::getDiscountType)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/grades")
    public ResponseEntity<List<String>> getUserGrade() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Arrays.stream(UserGrade.values())
                        .map(UserGrade::getUserGrade)
                        .collect(Collectors.toList()));
    }


    @GetMapping("/order")
    public ResponseEntity<List<CouponOrderResponseDto>> getMyOrderCoupons(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(value = "page", defaultValue = "1") int page,
                                                                          @RequestParam(value = "page", defaultValue = "10") int PAGE_SIZE, @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, Sort.by(Sort.Direction.ASC, sortBy));
        return ResponseEntity.status(HttpStatus.OK).body(couponService.getMyOrderCoupons(userDetails.getUser(), pageable));
    }


}