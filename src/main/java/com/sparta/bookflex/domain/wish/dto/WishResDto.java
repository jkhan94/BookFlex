package com.sparta.bookflex.domain.wish.dto;

import com.sparta.bookflex.domain.photoimage.entity.PhotoImage;
import com.sparta.bookflex.domain.wish.entity.Wish;
import lombok.Builder;
import lombok.Getter;

@Getter
public class WishResDto {
    Long wishId;
    String bookName;
    Long bookId;
    String photoUrl;

    @Builder
    public WishResDto(Long wishId, String bookName, Long bookId, String photoUrl) {
        this.wishId = wishId;
        this.bookName = bookName;
        this.bookId = bookId;
        this.photoUrl = photoUrl;
    }
}
