package com.sparta.bookflex.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonDto<T> {

    private int statusCode;
    private String message;
    private T data;

}
