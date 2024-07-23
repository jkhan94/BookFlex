package com.sparta.bookflex.domain.qna.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class QnaResponseDto {
    private String qnaType;
    private String inquiry;
    private LocalDateTime createdAt;
    private String reply;

    @Builder
    public QnaResponseDto(String qnaType, String inquiry, LocalDateTime createdAt, String reply) {
        this.qnaType = qnaType;
        this.inquiry = inquiry;
        this.createdAt = createdAt;
        this.reply = reply;
    }
}
