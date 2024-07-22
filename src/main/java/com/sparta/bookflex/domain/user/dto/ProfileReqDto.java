package com.sparta.bookflex.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProfileReqDto {

    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 하며 영문 대소문자와 특수문자를 1개이상 포함해야합니다")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{6,}$")
    private String password;

    private String nickname;

    @Pattern(regexp = "^(01[016789])-?[0-9]{3,4}-?[0-9]{4}$")
    private String phoneNumber;

    private String address;
}
