package com.sparta.bookflex.domain.user.controller;

import com.sparta.bookflex.common.config.JwtConfig;
import com.sparta.bookflex.common.dto.CommonDto;
import com.sparta.bookflex.common.jwt.JwtProvider;
import com.sparta.bookflex.domain.user.dto.LoginReqDto;
import com.sparta.bookflex.domain.user.dto.SignUpReqDto;
import com.sparta.bookflex.domain.user.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<CommonDto<Void>> signUp(@Valid @RequestBody SignUpReqDto signupReqDto) {
        authService.signUp(signupReqDto);
        CommonDto resDto = new CommonDto(HttpStatus.OK.value(), "회원가입이 완료되었습니다!", null);
        return ResponseEntity.ok().body(resDto);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonDto<Void>> login(@Valid @RequestBody LoginReqDto loginReqDto, HttpServletResponse response) {
        List<String> tokens = authService.login(loginReqDto);
        response.addHeader(JwtConfig.ACCESS_TOKEN_HEADER, tokens.get(0));
        response.addHeader(JwtConfig.REFRESH_TOKEN_HEADER,tokens.get(1));

        CommonDto resDto = new CommonDto(HttpStatus.OK.value(), "로그인이 완료되었습니다 !", null);
        return ResponseEntity.ok().body(resDto);
    }
}
