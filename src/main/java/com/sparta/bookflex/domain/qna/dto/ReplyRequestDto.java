package com.sparta.bookflex.domain.qna.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyRequestDto {

    @NotBlank(message = "문의에 대한 답변을 입력해주세요.")
    private String reply;
}
