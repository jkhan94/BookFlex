package com.sparta.bookflex.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.bookflex.domain.user.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SignUpReqDto {

    @NotNull(message = "id 값이 필수로 들어있어야 합니다.")
    @Size(min = 2, max = 10, message = "id는 최소 2자이상, 10자 이하이며 소문자와 숫자만 가능합니다")
    @Pattern(regexp = "^[a-z0-9]*$")
    private String username;

    @NotNull(message = "비밀번호는 필수로 입력해야 합니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 하며 영문 대소문자와 특수문자를 1개이상 포함해야합니다")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{6,}$")
    private String password;

    @Email(message = "Email 형식이어야 합니다.")
    private String email;

    @NotNull(message = "이름은 필수로 입력해야 합니다.")
    private String name;

    private String address;

    @NotNull(message = "핸드폰번호는 필수로 입력해야 합니다.")
    @Pattern(regexp = "^(01[016789])-?[0-9]{3,4}-?[0-9]{4}$")
    private String phoneNumber;

    private String nickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotNull(message = "role 값이 필수로 들어있어야 합니다.")
    private UserRole authType;

}