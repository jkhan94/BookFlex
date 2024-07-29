package com.sparta.bookflex.domain.user.dto;

import com.sparta.bookflex.domain.user.enums.RoleType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResDto {
    RoleType auth;
    private String accessToken;
    @Builder
    public LoginResDto(RoleType auth,String accessToken) {
        this.auth = auth;
        this.accessToken = accessToken;
    }
}
