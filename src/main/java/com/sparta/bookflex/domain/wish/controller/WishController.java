package com.sparta.bookflex.domain.wish.controller;

import com.sparta.bookflex.common.dto.CommonDto;
import com.sparta.bookflex.domain.wish.dto.WishResDto;
import com.sparta.bookflex.domain.wish.service.WishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishs")
public class WishController {

    WishService wishService;

    public WishController(WishService wIshService) {
        this.wishService = wIshService;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<?> createWish(
            @PathVariable Long bookId
            //, @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        Long userId = 1L;
        wishService.createWish(bookId, userId);
        CommonDto<Void> response = new CommonDto<>(HttpStatus.CREATED.value(), "위시리스트에 추가되었습니다.", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping()
    public ResponseEntity<?> getWishs(
            // @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = 1L;
        List<WishResDto> wislList =  wishService.getWishList(userId);
        CommonDto<List<WishResDto>> response = new CommonDto<>(HttpStatus.OK.value(), "위시리스트에 추가되었습니다.", wislList);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<?> deleteWish(
            @PathVariable Long wishId
    ) {
        Long userId = 1L;
        wishService.deleteWish(wishId, userId);
        CommonDto<Void> response = new CommonDto<>(HttpStatus.OK.value(), "위시리스트에서 삭제되었습니다.", null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }


}
