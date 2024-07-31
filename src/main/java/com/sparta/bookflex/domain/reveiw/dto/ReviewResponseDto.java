package com.sparta.bookflex.domain.reveiw.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ReviewResponseDto {

    private String title;

    private String content;

    private String star;

    private String username;

    private String bookName;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @Builder
    public ReviewResponseDto(String title,
                             String content,
                             String star,
                             String username,
                             String bookName,
                             LocalDateTime createdAt,
                             LocalDateTime modifiedAt) {

        this.title = title;
        this.content = content;
        this.star = star;
        this.username=username;
        this.bookName=bookName;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;

    }
}
