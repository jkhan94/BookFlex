package com.sparta.bookflex.domain.auth.dto;

import com.sparta.bookflex.domain.user.enums.RoleType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResDto {
    RoleType auth;
    private String accessToken;
    private String refreshToken;
    @Builder
    public LoginResDto(RoleType auth,String accessToken, String refreshToken) {
        this.auth = auth;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
