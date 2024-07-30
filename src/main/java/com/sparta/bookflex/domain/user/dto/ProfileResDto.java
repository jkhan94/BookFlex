package com.sparta.bookflex.domain.user.dto;

import com.sparta.bookflex.domain.user.enums.UserGrade;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class ProfileResDto {

    private String username;
    private String email;
    private String address;
    private String phoneNumber;
    private String nickname;
    private UserGrade grade;
    private LocalDate birthday;
    private LocalDateTime createdAt;
}
