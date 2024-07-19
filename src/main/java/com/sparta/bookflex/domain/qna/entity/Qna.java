package com.sparta.bookflex.domain.qna.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Qna extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String qnaType;

    @Column(nullable = false)
    String inquiry;

    @Column
    String reply;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    Book book;

    @Builder
    public Qna(String email, String qnaType, String inquiry, String reply, User user) {
        this.email = email;
        this.qnaType = qnaType;
        this.inquiry = inquiry;
        this.reply = reply;
        this.user = user;
    }
}