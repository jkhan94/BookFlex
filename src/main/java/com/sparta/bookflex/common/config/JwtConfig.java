package com.sparta.bookflex.common.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${jwt.access-time}")
    private Long ACCESS_TOKEN_TIME;
    @Value("${jwt.refresh-time}")
    private Long REFRESH_TOKEN_TIME;

    public static final String AUTHORIZATION_HEADER = "authorization";
    public static final String ACCESS_TOKEN_HEADER = "accessToken";
    public static final String REFRESH_TOKEN_HEADER = "refreshToken";
    public static final String EXPIRED_TOKEN = "expiredToken";
    public static final String BLOCKED_USER = "blockedUser";
    public static final String USER_STATE_KEY = "state";
    public static final String USER_GRADE_KEY = "grade";
    public static final String USER_AUTH_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";

    public static Key key;
    public static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    public static Long accessTokenTime;
    public static Long refreshTokenTime;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(SECRET_KEY);
        key = Keys.hmacShaKeyFor(bytes);
        accessTokenTime = ACCESS_TOKEN_TIME;
        refreshTokenTime = REFRESH_TOKEN_TIME;
    }
}
