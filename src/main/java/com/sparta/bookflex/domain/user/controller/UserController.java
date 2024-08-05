package com.sparta.bookflex.domain.user.controller;

import com.querydsl.core.Tuple;
import com.sparta.bookflex.common.aop.Envelop;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.book.entity.BookStatus;
import com.sparta.bookflex.domain.user.dto.*;
import com.sparta.bookflex.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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


    @GetMapping()
    public ResponseEntity<ProfileResDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        ProfileResDto resDto = userService.getProfile(userDetails.getUser());
        return ResponseEntity.ok().body(resDto);
    }

    @Envelop("프로필 수정에 성공하였습니다.")
    @PutMapping()
    public ResponseEntity<ProfileResDto> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails
            , @Valid @RequestBody ProfileReqDto reqDto) {

        ProfileResDto resDto = userService.updateProfile(userDetails.getUser(),
                reqDto);

        return ResponseEntity.ok().body(resDto);
    }

    @Envelop("회원 등급을 적용했습니다.")
    @PutMapping("/{userId}/grade")
    public ResponseEntity<Void> updateGrade(@PathVariable long userId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody GradeReqDto reqDto) {
        userService.updateGrade(userId, userDetails.getUser(), reqDto);
        return ResponseEntity.ok().body(null);
    }

    @Envelop("회원 상태를 변경했습니다.")
    @PutMapping("/{userId}/state")
    public ResponseEntity<Void> updateGrade(@PathVariable long userId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody StateReqDto reqDto) {
        userService.updateState(userId, userDetails.getUser(), reqDto);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<UserListResDto>> getUsers(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                @RequestParam(name = "direction", required = false, defaultValue = "true") boolean isAsc,
                                                @RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
                                                @RequestParam(name = "username", required = false) String username) {

       Page<UserListResDto> result = userService.getUsers(page, size, isAsc, sortBy, username);

        return ResponseEntity.ok().body(result);
    }


}
