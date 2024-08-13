package com.sparta.bookflex.domain.wish.controller;

import com.sparta.bookflex.common.dto.CommonDto;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.wish.dto.WishResDto;
import com.sparta.bookflex.domain.wish.service.WishService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishs")
public class WishController {

    WishService wishService;

    public WishController(WishService wIshService) {
        this.wishService = wIshService;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<?> createWish(
            @PathVariable Long bookId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        wishService.createWish(bookId, userDetails.getUser());
        CommonDto<Void> response = new CommonDto<>(HttpStatus.CREATED.value(), "위시리스트에 추가되었습니다.", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping()
    public ResponseEntity<?> getWishs(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable) {
        Page<WishResDto> wishList =  wishService.getWishList(userDetails.getUser(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(wishList);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<?> deleteWish(
            @PathVariable Long wishId
            , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        wishService.deleteWish(wishId, userDetails.getUser());
        CommonDto<Void> response = new CommonDto<>(HttpStatus.OK.value(), "위시리스트에서 삭제되었습니다.", null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }


}
