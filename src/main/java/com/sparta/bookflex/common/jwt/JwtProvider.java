package com.sparta.bookflex.common.jwt;

import com.sparta.bookflex.common.config.JwtConfig;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.enums.UserRole;
import com.sparta.bookflex.domain.user.enums.UserGrade;
import com.sparta.bookflex.domain.user.enums.UserState;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j(topic = "JwtProvider")
@Component
public class JwtProvider {

    public String createToken(User user, Long tokenTime) {
        Date date = new Date();

        String username = user.getUsername();
        UserState state = user.getState();
        UserGrade grade = user.getGrade();
        UserRole auth = user.getAuth();

        return JwtConfig.BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .setExpiration(new Date(date.getTime() + tokenTime))
                        .claim(JwtConfig.USER_STATE_KEY, state)
                        .claim(JwtConfig.USER_GRADE_KEY, grade)
                        .claim(JwtConfig.USER_AUTH_KEY, auth)
                        .setIssuedAt(date)
                        .signWith(JwtConfig.key, JwtConfig.signatureAlgorithm)
                        .compact();
    }

    public String getJwtFromHeader(HttpServletRequest request, String headerKey){
        return request.getHeader(headerKey);
    }

    public String substringToken(String token){
        if(!token.startsWith(JwtConfig.BEARER_PREFIX)){
            throw new IllegalArgumentException("유효하지 않은 토큰 값입니다.");
        }
        return token.substring(7);
    }

    public boolean isTokenValidate(String token,HttpServletRequest req) {
        try {
            Jwts.parserBuilder().setSigningKey(JwtConfig.key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            req.setAttribute(JwtConfig.EXPIRED_TOKEN,true);
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(JwtConfig.key).build().parseClaimsJws(token).getBody();
    }
}