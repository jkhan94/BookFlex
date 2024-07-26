package com.sparta.bookflex.domain.sale.dto;

import com.sparta.bookflex.domain.category.enums.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SaleVolumeRowDto {


    private String bookName;
    private String categoryName;
    private String publisher;
    private String author;
    private BigDecimal price;
    private int quantity;
    private BigDecimal totalPrice;

    @Builder
    public SaleVolumeRowDto(String bookName,
                            Category categoryName,
                            String publisher,
                            String author,
                            BigDecimal price,
                            int quantity,
                            BigDecimal totalPrice) {

        this.bookName = bookName;
        this.categoryName = categoryName.getCategoryName();
        this.publisher =  publisher;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
