package com.sparta.bookflex.domain.qna.dto;

import com.sparta.bookflex.domain.qna.enums.QnaTypeCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class QnaResponseDto {
    private QnaTypeCode qnaType;
    private String inquiry;
    private LocalDateTime createdAt;
    private String reply;

    @Builder
    public QnaResponseDto(QnaTypeCode qnaType, String inquiry, LocalDateTime createdAt, String reply) {
        this.qnaType = qnaType;
        this.inquiry = inquiry;
        this.createdAt = createdAt;
        this.reply = reply;
    }
}
