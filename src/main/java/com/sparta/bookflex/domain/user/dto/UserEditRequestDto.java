package com.sparta.bookflex.domain.user.dto;

import com.sparta.bookflex.domain.user.enums.UserGrade;
import com.sparta.bookflex.domain.user.enums.UserState;
import lombok.Getter;

@Getter
public class UserEditRequestDto {

    private UserState state;
    private UserGrade grade;

}
