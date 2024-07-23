package com.sparta.bookflex.domain.qna.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QnaRequestDto {
    @NotBlank
    private String email;

    @NotBlank
    private String qnaType;

    @NotBlank
    private String inquiry;
}
