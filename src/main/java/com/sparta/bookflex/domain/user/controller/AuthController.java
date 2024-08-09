package com.sparta.bookflex.domain.user.controller;

import com.sparta.bookflex.common.aop.Envelop;
import com.sparta.bookflex.common.config.JwtConfig;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.user.dto.LoginReqDto;
import com.sparta.bookflex.domain.user.dto.LoginResDto;
import com.sparta.bookflex.domain.user.dto.RefreshResDto;
import com.sparta.bookflex.domain.user.dto.SignUpReqDto;
import com.sparta.bookflex.domain.user.service.AuthService;
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

    @Envelop("가입되었습니다.")
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
        response.addHeader(JwtConfig.REFRESH_TOKEN_HEADER,refreshToken);

        LoginResDto loginResDto = LoginResDto.builder()
                .auth(authService.getUserRole(loginReqDto.getUsername()))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return ResponseEntity.ok().body(loginResDto);
    }

    @Envelop("탈퇴했습니다. 그동안 이용해주셔서 감사합니다.")
    @PutMapping("/signout")
    public ResponseEntity<Void> signOut(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response){

        authService.signOut(userDetails.getUser());
        response.setHeader(JwtConfig.ACCESS_TOKEN_HEADER, "");
        response.setHeader(JwtConfig.REFRESH_TOKEN_HEADER, "");

        return ResponseEntity.ok().body(null);
    }

    @Envelop("로그아웃에 성공하였습니다.")
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response){
        authService.logout(userDetails.getUser());
        response.setHeader(JwtConfig.ACCESS_TOKEN_HEADER, "");
        response.setHeader(JwtConfig.REFRESH_TOKEN_HEADER, "");

        return ResponseEntity.ok().body(null);
    }

    @Envelop("토큰을 재발급 하였습니다.")
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResDto> refreshToken(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response, HttpServletRequest request)
    {
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
