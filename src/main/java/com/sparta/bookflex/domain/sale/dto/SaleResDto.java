package com.sparta.bookflex.domain.sale.dto;

import com.sparta.bookflex.domain.photoimage.entity.PhotoImage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SaleResDto {

    private Long saleId;
    //private PhotoImage photoImage;
    private String bookName;
    private int price;
    //private Boolean isReviewed;
    private int quantity;
    private String status;
    private LocalDateTime createdAt;

    @Builder
    public SaleResDto(Long saleId, String bookName, int price, int quantity, String status, LocalDateTime createdAt) {
        this.saleId = saleId;
        this.bookName = bookName;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.createdAt = createdAt;
    }

}
