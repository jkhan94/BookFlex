package com.sparta.bookflex.domain.sale.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SaleResponseDto {

    private Long saleId;
    //private PhotoImage photoImage;
    private String bookName;
    private int price;
    //private Boolean isReviewed;
    private int quantity;
    private int total;
    private String status;
    private LocalDateTime createdAt;

    @Builder
    public SaleResponseDto(Long saleId, String bookName, int price, int quantity, String status, LocalDateTime createdAt, int total) {
        this.saleId = saleId;
        this.bookName = bookName;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.createdAt = createdAt;
        this.total = total;
    }


    @Builder
    public SaleResponseDto(Long saleId, String bookName, int price, int quantity, String status, LocalDateTime createdAt) {
        this.saleId = saleId;
        this.bookName = bookName;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.createdAt = createdAt;
    }
}
