package com.sparta.bookflex.domain.book.dto;

import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.category.enums.Category;
import com.sparta.bookflex.domain.book.entity.BookStatus;
import com.sparta.bookflex.domain.photoimage.entity.PhotoImage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class BookRequestDto {

    @NotBlank(message = "책 이름을 입력해주세요.")
    private String bookName;

    @NotBlank(message = "출판사를 입력해주세요.")
    private String publisher;

    @NotBlank(message = "작가명을 입력해주세요.")
    private String author;

    //    @NotBlank(message = "가격을 입력해주세요.")
//    @Pattern(regexp = "^[0-9]*$", message = "숫자만 입력 가능합니다.")
    private BigDecimal price;

    //    @NotBlank(message = "재고량을 입력해주세요.")
//    @Pattern(regexp = "^[0-9]*$", message = "숫자만 입력 가능합니다.")
    private int stock;

    @NotBlank(message = "책 설명을 입력해주세요.")
    private String bookDescription;

    @NotBlank(message = "카테고리를 지정해주세요.")
    private String mainCategory;

    @NotBlank(message = "카테고리를 지정해주세요.")
    private String subCategory;


    public Book toEntity(PhotoImage photoImage) {
        return Book.builder()
                .bookName(this.bookName)
                .publisher(this.publisher)
                .author(this.author)
                .price(this.price)
                .stock(this.stock)
                .bookDescription(this.bookDescription)
                .mainCategory(Category.findCategoryByName(this.mainCategory))
                .subCategory(Category.findCategoryByName(this.subCategory))
                .photoImage(photoImage)
                .build();
    }

}
