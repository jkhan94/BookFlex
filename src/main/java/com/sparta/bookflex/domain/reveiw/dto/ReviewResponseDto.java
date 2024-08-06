package com.sparta.bookflex.domain.reveiw.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ReviewResponseDto {

    private Long id;

    private String title;

    private String content;

    private int star;

    private String username;

    private String nickname;

    private String bookName;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @Builder
    public ReviewResponseDto(Long id,
                             String title,
                             String content,
                             int star,
                             String username,
                             String nickname,
                             String bookName,
                             LocalDateTime createdAt,
                             LocalDateTime modifiedAt) {

        this.id = id;
        this.title = title;
        this.content = content;
        this.star = star;
        this.username = username;
        this.nickname = nickname;
        this.bookName = bookName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;

    }
}
