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

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
//    private final SocialService socialService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpReqDto signupReqDto) {
        authService.signUp(signupReqDto);
        return ResponseEntity.ok().body(null);
    }

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

    @PutMapping("/signout")
    public ResponseEntity<Void> signOut(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {

        authService.signOut(userDetails.getUser());
        response.setHeader(JwtConfig.ACCESS_TOKEN_HEADER, "");
        response.setHeader(JwtConfig.REFRESH_TOKEN_HEADER, "");

        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        authService.logout(userDetails.getUser());
        response.setHeader(JwtConfig.ACCESS_TOKEN_HEADER, "");
        response.setHeader(JwtConfig.REFRESH_TOKEN_HEADER, "");

        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResDto> refreshToken(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response, HttpServletRequest request) {
        String accessToken = authService.refreshToken(userDetails.getUser(), request.getHeader(JwtConfig.AUTHORIZATION_HEADER));
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
