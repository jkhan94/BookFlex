package com.sparta.bookflex.domain.auth.controller;

import com.sparta.bookflex.common.config.JwtConfig;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.auth.dto.LoginReqDto;
import com.sparta.bookflex.domain.auth.dto.LoginResDto;
import com.sparta.bookflex.domain.auth.dto.RefreshResDto;
import com.sparta.bookflex.domain.auth.dto.SignUpReqDto;
import com.sparta.bookflex.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 인증 관련 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 이 컨트롤러는 회원가입, 로그인, 로그아웃, 토큰 갱신 등의 인증 작업을 담당합니다.
 * </p>
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
//    private final SocialService socialService;

    /**
     * 회원가입을 처리합니다.
     *
     * @param signupReqDto 회원가입 요청 정보를 담고 있는 DTO
     * @return 회원가입 성공 시 빈 본문으로 응답합니다.
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpReqDto signupReqDto) {
        authService.signUp(signupReqDto);
        return ResponseEntity.ok().body(null);
    }

    /**
     * 로그인을 처리합니다.
     *
     * @param loginReqDto 로그인 요청 정보를 담고 있는 DTO
     * @param response    HTTP 응답 객체로, 여기서 액세스 토큰과 리프레시 토큰을 헤더에 추가합니다.
     * @return 로그인 성공 시 로그인 응답 DTO를 반환합니다.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@Valid @RequestBody LoginReqDto loginReqDto, HttpServletResponse response) {
        List<String> tokens = authService.login(loginReqDto);
        String accessToken = tokens.get(0);
        String refreshToken = tokens.get(1);
        response.addHeader(JwtConfig.ACCESS_TOKEN_HEADER, accessToken);
        response.addHeader(JwtConfig.REFRESH_TOKEN_HEADER, refreshToken);

        LoginResDto loginResDto = LoginResDto.builder()
                .auth(authService.getUserRole(loginReqDto.getUsername()))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return ResponseEntity.ok().body(loginResDto);
    }

    /**
     * 사용자 회원탈퇴를 처리합니다.
     *
     * @param userDetails 인증된 사용자 정보를 포함한 객체
     * @param response    HTTP 응답 객체로, 여기서 액세스 토큰과 리프레시 토큰을 헤더에서 제거합니다.
     * @return 회원탈퇴 성공시 반환
     */
    @PutMapping("/signout")
    public ResponseEntity<Void> signOut(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {

        authService.signOut(userDetails.getUser());
        response.setHeader(JwtConfig.ACCESS_TOKEN_HEADER, "");
        response.setHeader(JwtConfig.REFRESH_TOKEN_HEADER, "");

        return ResponseEntity.ok().body(null);
    }

    /**
     * 사용자 로그아웃을 처리합니다.
     *
     * @param userDetails 인증된 사용자 정보를 포함한 객체
     * @param response    HTTP 응답 객체로, 여기서 액세스 토큰과 리프레시 토큰을 헤더에서 제거합니다.
     * @return 로그아웃 성공 시 빈 본문으로 응답합니다.
     */
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        authService.logout(userDetails.getUser());
        response.setHeader(JwtConfig.ACCESS_TOKEN_HEADER, "");
        response.setHeader(JwtConfig.REFRESH_TOKEN_HEADER, "");

        return ResponseEntity.ok().body(null);
    }

    /**
     * 리프레시 토큰을 이용해 새로운 액세스 토큰을 발급합니다.
     *
     * @param userDetails 인증된 사용자 정보를 포함한 객체
     * @param response    HTTP 응답 객체로, 여기서 새로운 액세스 토큰을 헤더에 추가할 수 있습니다.
     * @param request     HTTP 요청 객체로, 여기서 기존 리프레시 토큰을 헤더에서 가져옵니다.
     * @return 새로운 액세스 토큰을 포함한 응답 DTO를 반환합니다.
     */
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResDto> refreshToken(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response, HttpServletRequest request) {
        String accessToken = authService.refreshToken(userDetails.getUser(), request.getHeader(JwtConfig.REFRESH_TOKEN_HEADER));
        RefreshResDto resDto = new RefreshResDto(accessToken);

        return ResponseEntity.ok().body(resDto);
    }

//    @GetMapping("/kakao/callback")
//    public ResponseEntity<String> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
//        List<String> tokens = socialService.kakaoLogin(code);
//        response.addHeader(JwtConfig.ACCESS_TOKEN_HEADER, tokens.get(0));
//        response.addHeader(JwtConfig.REFRESH_TOKEN_HEADER,tokens.get(1));// response header에 access token 넣기
//
//
//        return ResponseEntity.status(HttpStatus.OK).body("카카오 로그인 하였습니다.");
//    }
}
