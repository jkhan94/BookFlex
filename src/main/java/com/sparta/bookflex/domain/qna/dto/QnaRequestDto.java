package com.sparta.bookflex.domain.qna.dto;

import com.sparta.bookflex.common.aop.ValidEnum;
import com.sparta.bookflex.domain.qna.enums.QnaTypeCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QnaRequestDto {
    @Email(message = "이메일을 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @ValidEnum(enumClass = QnaTypeCode.class, message = "문의 유형을 선택해주세요.")
    private QnaTypeCode qnaType;

    @NotBlank(message = "문의 내용을 입력해주세요.")
    private String inquiry;
}
