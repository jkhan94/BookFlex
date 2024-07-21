package com.sparta.bookflex.common.security;

import com.sparta.bookflex.common.config.JwtConfig;
import com.sparta.bookflex.common.jwt.JwtProvider;
import com.sparta.bookflex.domain.user.enums.UserState;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Slf4j(topic = "jwtAuthenticationFilter")
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtProvider.getJwtFromHeader(req, JwtConfig.AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(token)) {
            log.info("토큰 아예 없는 경우");
            filterChain.doFilter(req, res);
            return;
        }

        token = jwtProvider.substringToken(token);

        if (!jwtProvider.isTokenValidate(token, req)) {
            log.info("정상 토큰 없는 상태");
            filterChain.doFilter(req, res);
            return;
        }

        log.info("정상 토큰 있는 상태");
        Claims userInfo = jwtProvider.getUserInfoFromToken(token);
        String userState = userInfo.get(JwtConfig.USER_STATE_KEY, String.class);

        if(userState.equals(UserState.WITHDRAW.getStateString())) {
            log.info("유저가 탈퇴한 상태");
            throw new AccessDeniedException("해당 유저는 탈퇴상태입니다");
        }

        setAuthentication(userInfo.getSubject());

        filterChain.doFilter(req, res);
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    public Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
