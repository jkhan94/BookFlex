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

/**
 * 사용자 관리와 관련된 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 이 컨트롤러는 사용자 프로필 조회, 업데이트, 사용자 목록 조회 및 등급 변경 등의 작업을 담당합니다.
 * </p>
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final UserRoleScheduler userRoleScheduler;


    /**
     * 현재 인증된 사용자의 프로필을 조회합니다.
     *
     * @param userDetails 현재 인증된 사용자 정보를 포함한 객체
     * @return 조회된 프로필 정보를 담은 ProfileResDto 객체를 반환합니다.
     */
    @GetMapping()
    public ResponseEntity<ProfileResDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        ProfileResDto resDto = userService.getProfile(userDetails.getUser());
        return ResponseEntity.ok().body(resDto);
    }

    /**
     * 현재 인증된 사용자의 프로필을 업데이트합니다.
     *
     * @param userDetails 현재 인증된 사용자 정보를 포함한 객체
     * @param reqDto      프로필 업데이트 요청 정보를 담고 있는 DTO
     * @return 업데이트된 프로필 정보를 담은 ProfileResDto 객체를 반환합니다.
     */
    @PutMapping()
    public ResponseEntity<ProfileResDto> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails
            , @Valid @RequestBody ProfileReqDto reqDto) {

        ProfileResDto resDto = userService.updateProfile(userDetails.getUser(),
                reqDto);

        return ResponseEntity.ok().body(resDto);
    }

    /**
     * 특정 사용자의 등급을 업데이트합니다.
     *
     * @param userId      등급을 업데이트할 사용자 ID
     * @param userDetails 현재 인증된 사용자 정보를 포함한 객체
     * @param reqDto      등급 업데이트 요청 정보를 담고 있는 DTO
     * @return 등급 업데이트 성공 시 빈 본문으로 응답합니다.
     */
    @PutMapping("/{userId}/grade")
    public ResponseEntity<Void> updateGrade(@PathVariable long userId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody GradeReqDto reqDto) {
        userService.updateGrade(userId, userDetails.getUser(), reqDto);
        return ResponseEntity.ok().body(null);
    }

    /**
     * 특정 사용자의 상태를 업데이트합니다.
     *
     * @param userId 사용자의 상태를 업데이트할 사용자 ID
     * @param userDetails 현재 인증된 사용자 정보를 포함한 객체
     * @param reqDto 상태 업데이트 요청 정보를 담고 있는 DTO
     * @return 상태 업데이트 성공 시 빈 본문으로 응답합니다.
     */
    @PutMapping("/{userId}/state")
    public ResponseEntity<Void> updateGrade(@PathVariable long userId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody StateReqDto reqDto) {
        userService.updateState(userId, reqDto);
        return ResponseEntity.ok().body(null);
    }

    /**
     * 모든 사용자 목록을 페이지네이션, 정렬 및 필터링 조건에 따라 조회합니다.
     *
     * @param page     조회할 페이지 번호 (기본값: 1)
     * @param size     페이지 당 항목 수 (기본값: 10)
     * @param isAsc    오름차순 정렬 여부 (true: 오름차순, false: 내림차순)
     * @param sortBy   정렬 기준 필드 (기본값: createdAt)
     * @param username 필터링할 사용자 이름 (옵션)
     * @return 조회된 사용자 목록을 담은 Page<UserListResDto> 객체를 반환합니다.
     */
    @GetMapping("/all")
    public ResponseEntity<Page<UserListResDto>> getUsers(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                         @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                         @RequestParam(name = "direction", required = false, defaultValue = "true") boolean isAsc,
                                                         @RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
                                                         @RequestParam(name = "username", required = false) String username) {

        Page<UserListResDto> result = userService.getUsers(page, size, isAsc, sortBy, username);

        return ResponseEntity.ok().body(result);
    }

    /**
     * 특정 사용자의 프로필을 조회합니다.
     *
     * @param userId 조회할 사용자 ID
     * @return 조회된 프로필 정보를 담은 ProfileResDto 객체를 반환합니다.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResDto> getUsers(@PathVariable(value = "userId") Long userId) {

        ProfileResDto result = userService.getUserProfile(userId);

        return ResponseEntity.ok().body(result);
    }
}
