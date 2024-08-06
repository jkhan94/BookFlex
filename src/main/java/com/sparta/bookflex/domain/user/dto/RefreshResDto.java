package com.sparta.bookflex.domain.user.dto;

import lombok.Getter;

@Getter
public class RefreshResDto {
    private String accessToken;

    public RefreshResDto(String token) {
        this.accessToken = token;
    }
}
