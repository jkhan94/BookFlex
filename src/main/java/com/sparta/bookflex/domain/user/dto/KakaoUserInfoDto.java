package com.sparta.bookflex.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate birthDay;

    public KakaoUserInfoDto(Long id,
                            String name,
                            String nickname,
                            String email,
                            String phoneNumber,
                            String address,
                            LocalDate birthDay) {

        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthDay = birthDay;
    }


    public KakaoUserInfoDto(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }


}
