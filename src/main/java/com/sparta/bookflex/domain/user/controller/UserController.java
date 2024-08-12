package com.sparta.bookflex.domain.user.controller;

import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.user.dto.*;
import com.sparta.bookflex.domain.user.scheduler.UserRoleScheduler;
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

    private final UserRoleScheduler userRoleScheduler;


    @GetMapping()
    public ResponseEntity<ProfileResDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        ProfileResDto resDto = userService.getProfile(userDetails.getUser());
        return ResponseEntity.ok().body(resDto);
    }

    @PutMapping()
    public ResponseEntity<ProfileResDto> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails
            , @Valid @RequestBody ProfileReqDto reqDto) {

        ProfileResDto resDto = userService.updateProfile(userDetails.getUser(),
                reqDto);

        return ResponseEntity.ok().body(resDto);
    }

    @PutMapping("/{userId}/grade")
    public ResponseEntity<Void> updateGrade(@PathVariable long userId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody GradeReqDto reqDto) {
        userService.updateGrade(userId, userDetails.getUser(), reqDto);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/{userId}/state")
    public ResponseEntity<Void> updateGrade(@PathVariable long userId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody StateReqDto reqDto) {
        userService.updateState(userId, reqDto);
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

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResDto> getUsers(@PathVariable(value = "userId") Long userId) {

        ProfileResDto result = userService.getUserProfile(userId);

        return ResponseEntity.ok().body(result);
    }
}
