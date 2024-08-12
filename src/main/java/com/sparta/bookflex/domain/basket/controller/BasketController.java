package com.sparta.bookflex.domain.basket.controller;

import com.sparta.bookflex.common.dto.CommonDto;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.basket.dto.BasketCreateReqestDto;
import com.sparta.bookflex.domain.basket.dto.BasketItemResponseDto;
import com.sparta.bookflex.domain.basket.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/baskets")
public class BasketController {
    private final BasketService basketService;

    @Autowired
    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @PostMapping
    public ResponseEntity<CommonDto<Void>> createBasketitem(
            @RequestBody BasketCreateReqestDto basketCreateDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails){

        basketService.createBasketItem(basketCreateDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonDto<>(HttpStatus.CREATED.value(), "장바구니에 추가되었습니다.", null));

    }


    @GetMapping("")
    public ResponseEntity<?> getBasketItems(
            @AuthenticationPrincipal UserDetailsImpl userDetails,

            @PageableDefault(size = 10)Pageable pageable){
        Page<BasketItemResponseDto> basketResDto = basketService.getBasketItems(userDetails.getUser(), pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "장바구니를 조회했습니다.", basketResDto));
    }

    @PutMapping("/{basketItemId}")
    public ResponseEntity<CommonDto<BasketItemResponseDto>> updateBasket(
        @PathVariable Long basketItemId,
        @RequestParam int quantity,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        BasketItemResponseDto basketResDto =  basketService.updateBasket(basketItemId, quantity, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "장바구니의 상품 수량을 수정했습니다.",basketResDto));
    }

    @DeleteMapping("/{basketItemId}")
    public ResponseEntity<CommonDto<Void>> deleteBasket(
        @PathVariable Long basketItemId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        basketService.deleteBasketItem(basketItemId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new CommonDto<>(HttpStatus.NO_CONTENT.value(), "장바구니을 삭제했습니다.", null));
    }



}
