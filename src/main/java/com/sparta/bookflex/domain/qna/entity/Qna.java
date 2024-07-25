package com.sparta.bookflex.domain.qna.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.qna.dto.ReplyRequestDto;
import com.sparta.bookflex.domain.qna.enums.QnaTypeCode;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Qna extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QnaTypeCode qnaType;

    @Column(nullable = false)
    private String inquiry;

    @Column
    private String reply;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Qna(String email, QnaTypeCode qnaType, String inquiry, String reply, User user) {
        this.email = email;
        this.qnaType = qnaType;
        this.inquiry = inquiry;
        this.reply = reply;
        this.user = user;
    }

    public void updateReply(Qna qna, ReplyRequestDto requestDto) {
        this.reply = requestDto.getReply();
    }
}