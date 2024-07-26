package com.sparta.bookflex.domain.coupon.controller;

import com.sparta.bookflex.common.aop.Envelop;
import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.coupon.dto.CouponRequestDto;
import com.sparta.bookflex.domain.coupon.dto.CouponResponseDto;
import com.sparta.bookflex.domain.coupon.dto.CouponStatusRequestDto;
import com.sparta.bookflex.domain.coupon.service.CouponService;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.enums.RoleType;
import com.sparta.bookflex.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        //  ADMIN인지
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.ADMIN) {
            throw new BusinessException(COUPON_CREATE_NOT_ALLOWED);
        }

        // 날짜 검증 startDate < expirationDate
        if(requestDto.getStartDate().isAfter(requestDto.getExpirationDate())){
            throw new BusinessException(COUPON_CREATE_NOT_ALLOWED);
        }

        CouponResponseDto responseDto = couponService.createCoupon(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


/*   쿠폰 사용자 조회
       GET /coupons

       응답DTO
       statusCode	200
       message	“사용 가능한 모든 쿠폰을 조회했습니다.”
       data	{
           couponId	쿠폰ID
           couponName	쿠폰이름
           couponCode	쿠폰코드
           minPrice	최소주문금액
           discountPrice	할인금액
           startDate	쿠폰 사용 시작일자
           expirationDate	만료일자
           },
   */
    @GetMapping
    @Envelop("사용 가능한 모든 쿠폰을 조회했습니다.")
    public ResponseEntity<List<CouponResponseDto>> getMyCoupons(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                                @RequestParam(value = "sortBy", defaultValue = "expirationDate") String sortBy) {
//        USER인지
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.USER) {
            throw new BusinessException(COUPON_VIEW_NOT_ALLOWED);
        }

        List<CouponResponseDto> responseList = couponService.getMyCoupons(user, page - 1, sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }



/*
    쿠폰 관리자 조회
    GET /coupons/admin

    응답DTO
    statusCode	200
    message	“발급 가능한 모든 쿠폰을 조회했습니다.”
    data	{
        couponId	쿠폰ID
        couponName	쿠폰이름
        couponStatus	쿠폰상태
        totalCount	발급 가능한 쿠폰 개수
        minPrice	최소주문금액
        discountPrice	할인금액
        startDate	쿠폰 사용 시작일자
        expirationDate	만료일자
        createAt	생성일자
        modifedAt	수정일자
        },
*/

    @GetMapping("/admin")
    @Envelop("발급 가능한 모든 쿠폰을 조회했습니다.")
    public ResponseEntity<List<CouponResponseDto>> getAllCoupons(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                                                 @RequestParam(value = "sortBy", defaultValue = "expirationDate") String sortBy) {
//        ADMIN인지
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.ADMIN) {
            throw new BusinessException(COUPON_VIEW_NOT_ALLOWED);
        }

        List<CouponResponseDto> responseList = couponService.getAllCoupons(page - 1, sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }



/*
   쿠폰 상태 변경
   PUT /coupons/{couponId}

   요청DTO
   couponStatus	쿠폰 상태

   응답DTO
   statusCode	200
   message	쿠폰 상태를 변경했습니다.
   data	{
       couponId		쿠폰ID
       couponName	쿠폰이름
       couponStatus	쿠폰상태
       totalCount	발급 가능한 쿠폰 개수
       minPrice		최소주문금액
       discountPrice	할인금액
       startDate		쿠폰 사용 시작일자
       expirationDate	만료일자
       createAt		생성일자
       modifedAt	수정일자
   }
*/

    @PutMapping("/{couponId}")
    public ResponseEntity<CouponResponseDto> updateCouponStatus(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                @RequestBody @Valid CouponStatusRequestDto requestDto,
                                                                @PathVariable long couponId) {
        // ADMIN인지
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.ADMIN) {
            throw new BusinessException(COUPON_UPDATE_NOT_ALLOWED);
        }

        CouponResponseDto responseDto = couponService.updateCouponStatus(couponId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    /*
        쿠폰 삭제
        DELETE /coupons/{couponId}

        응답DTO
        statusCode	204
        message	“쿠폰을 삭제했습니다.”
    */
    @DeleteMapping("/{couponId}")
    public ResponseEntity<?> deleteCoupon(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @PathVariable long couponId) {
        // ADMIN인지
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.ADMIN) {
            throw new BusinessException(COUPON_DELETE_NOT_ALLOWED);
        }

        couponService.deleteCoupon(couponId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /*
        쿠폰 발급
        POST /coupons/{couponId}

        응답DTO
        statusCode	201
        message		쿠폰이 발급되었습니다.
        data	couponId	쿠폰ID
                couponName	쿠폰이름
                couponCode	쿠폰코드
                couponStatus	쿠폰상태
                minPrice		최소주문금액
                discountPrice	할인금액
                startDate		쿠폰 사용 시작일자
                expirationDate	만료일자
    */
    @PostMapping("/{couponId}")
    public ResponseEntity<CouponResponseDto> issueCoupon(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @PathVariable long couponId) {
        // User인지
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.USER) {
            throw new BusinessException(COUPON_ISSUE_NOT_ALLOWED);
        }

        CouponResponseDto responseDto = couponService.issueCoupon(couponId, user);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    /*
        사용할 쿠폰 조회
        GET /coupons/use/{couponId}

        응답DTO
        statusCode	200
        message	사용할 쿠폰을 조회했습니다.
        data	couponId	쿠폰ID
                couponName	쿠폰이름
                couponCode	쿠폰코드
                minPrice	최소주문금액
                discountPrice	할인금액
                startDate	쿠폰 사용 시작일자
                expirationDate	만료일자
    */
    @GetMapping("/use/{couponId}")
    public ResponseEntity<CouponResponseDto> getSingleUserCoupon(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @PathVariable long couponId) {
        // USER인지
        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.USER) {
            throw new BusinessException(COUPON_VIEW_NOT_ALLOWED);
        }

        CouponResponseDto responseDto = couponService.getSingleUserCoupon(couponId, user);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


}

