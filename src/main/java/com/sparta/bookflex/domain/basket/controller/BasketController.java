package com.sparta.bookflex.domain.basket.controller;

import com.sparta.bookflex.common.dto.CommonDto;
import com.sparta.bookflex.domain.basket.dto.BasketCreateReqDto;
import com.sparta.bookflex.domain.basket.dto.BasketResDto;
import com.sparta.bookflex.domain.basket.service.BasketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/baskets")
public class BasketController {
    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @PostMapping
    public ResponseEntity<CommonDto<Void>> createBasket(
            @RequestBody BasketCreateReqDto basketCreateDto
        //, @AuthenticationPrincipal UserDetailsimpl userDetails
            ) {
        Long userId = 1L;
        basketService.createBasket(basketCreateDto, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonDto<>(HttpStatus.CREATED.value(), "장바구니에 추가되었습니다.", null));

    }

    @GetMapping("/{bookId}")
    public ResponseEntity<CommonDto<List<BasketResDto>>> getBasket(
        @PathVariable Long bookId
        //,@AuthenticationPrincipal UserDetailsimpl userDetails
    ){
        Long userId = 1L;
        List<BasketResDto> basketResDto = basketService.getBaskets(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "장바구니를 조회했습니다.", basketResDto));
    }

    @PutMapping("/{basketId")
    public ResponseEntity<CommonDto<BasketResDto>> updateBasket(
        @PathVariable Long basketId,
        @RequestParam int quantity
        ///,@AuthenticationPrincipal UserDetailsimpl userDetails
    ){
        Long userId = 1L;
        BasketResDto basketResDto =  basketService.updateBasket(basketId, quantity, userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "장바구니의 상품 수량을 수정했습니다.",basketResDto));
    }

    @DeleteMapping("/{basketId}")
    public ResponseEntity<CommonDto<Void>> deleteBasket(
        @PathVariable Long basketId
        //,@AuthenticationPrincipal UserDetailsimpl userDetails
    ){
        Long userId = 1L;
        basketService.deleteBasket(basketId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new CommonDto<>(HttpStatus.NO_CONTENT.value(), "장바구니을 삭제했습니다.", null));
    }



}
