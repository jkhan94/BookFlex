package com.sparta.bookflex.domain.book.entity;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.exception.ErrorCode;

import java.util.Arrays;

public enum BookStatus {
    ONSALE("판매 중"),
    SOLDOUT("품절");

    private final String status;

    BookStatus(String status) {
        this.status = status;
    }

    public static BookStatus of(String status) {
        return Arrays.stream(values())
                .filter(bookStatus -> status.equals(bookStatus.status))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOKSTATUS_NOT_FOUND));
    }

    public String getStatusValue(){
        return this.status;
    }
}
