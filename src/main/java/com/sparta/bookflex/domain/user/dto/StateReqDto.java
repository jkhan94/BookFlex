package com.sparta.bookflex.domain.user.dto;

import com.sparta.bookflex.domain.user.enums.UserState;
import lombok.Getter;

@Getter
public class StateReqDto {

    private UserState state;
}
