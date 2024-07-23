package com.sparta.bookflex.domain.user.controller;

import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.user.dto.ProfileReqDto;
import com.sparta.bookflex.domain.user.dto.ProfileResDto;
import com.sparta.bookflex.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResDto> getProfile(@PathVariable long userId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        ProfileResDto resDto = userService.getProfile(userId, userDetails.getUser());
        return ResponseEntity.ok().body(resDto);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ProfileResDto> updateProfile(@PathVariable long userId, @AuthenticationPrincipal UserDetailsImpl userDetails
    ,@Valid @RequestBody ProfileReqDto reqDto) {

        ProfileResDto resDto = userService.updateProfile(userId, userDetails.getUser(),
            reqDto);

        return ResponseEntity.ok().body(resDto);
    }
}
