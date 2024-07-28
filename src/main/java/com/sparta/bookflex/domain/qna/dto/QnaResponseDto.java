package com.sparta.bookflex.domain.qna.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.bookflex.domain.qna.enums.QnaTypeCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class QnaResponseDto {
    private long qnaId;
    private QnaTypeCode qnaType;
    private String inquiry;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    private String reply;

    @Builder
    public QnaResponseDto(long qnaId, QnaTypeCode qnaType, String inquiry, LocalDateTime createdAt, String reply) {
        this.qnaId = qnaId;
        this.qnaType = qnaType;
        this.inquiry = inquiry;
        this.createdAt = createdAt;
        this.reply = reply;
    }
}
