package com.sparta.bookflex.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bookflex.common.config.JwtConfig;
import com.sparta.bookflex.common.dto.CommonDto;
import com.sparta.bookflex.common.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        CommonDto resDto;
        if(request.getAttribute(JwtConfig.EXPIRED_TOKEN) != null && (boolean) request.getAttribute(JwtConfig.EXPIRED_TOKEN)){
            response.setStatus(ErrorCode.EXPIRED_TOKEN.getStatus().value());
            resDto = new CommonDto(ErrorCode.EXPIRED_TOKEN.getStatus().value(), ErrorCode.EXPIRED_TOKEN.getMessage(), null);
        }else if(request.getAttribute(JwtConfig.BLOCKED_USER) != null && (boolean) request.getAttribute(JwtConfig.BLOCKED_USER)) {
            response.setStatus(ErrorCode.USER_BANNED.getStatus().value());
            resDto = new CommonDto(ErrorCode.USER_BANNED.getStatus().value(), ErrorCode.USER_BANNED.getMessage(), null);
        }else{
            response.setStatus(ErrorCode.USER_NOT_AUTHENTICATED.getStatus().value());
            resDto = new CommonDto(ErrorCode.USER_NOT_AUTHENTICATED.getStatus().value(), ErrorCode.USER_NOT_AUTHENTICATED.getMessage(), null);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(resDto));
    }
}
