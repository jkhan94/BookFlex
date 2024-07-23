package com.sparta.bookflex.domain.wish.dto;

import com.sparta.bookflex.domain.photoimage.entity.PhotoImage;
import com.sparta.bookflex.domain.wish.entity.Wish;
import lombok.Builder;
import lombok.Getter;

@Getter
public class WishResDto {
    Long wishId;
    //PhotoImage photoImage;
    String bookName;

    @Builder
    public WishResDto(Long wishId, String bookName) {
        this.wishId = wishId;
        //this.photoImage = wish.getBook().getPhotoImage();
        this.bookName = bookName;
    }
}
