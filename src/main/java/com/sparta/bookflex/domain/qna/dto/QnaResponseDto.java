package com.sparta.bookflex.domain.qna.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.bookflex.domain.qna.enums.QnaTypeCode;
import com.sparta.bookflex.domain.user.entity.User;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    private String reply;

    private String username;

    @Builder
    public QnaResponseDto(long qnaId, QnaTypeCode qnaType, String inquiry, LocalDateTime createdAt, String reply, String username) {
        this.qnaId = qnaId;
        this.qnaType = qnaType;
        this.inquiry = inquiry;
        this.createdAt = createdAt;
        this.reply = reply;
        this.username = username;
    }

}
