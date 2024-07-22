package com.sparta.bookflex.domain.book.dto;

import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.category.entity.Category;
import com.sparta.bookflex.domain.photoimage.entity.PhotoImage;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class BookRequestDto {

   private String bookName;
   private String publisher;
   private String author;
   private int price;
   private int stock;
   private String bookDescription;
   private String status;
   private Category category;

    public Book toEntity(PhotoImage photoImage) {
        return Book.builder()
                .bookName(this.bookName)
                .publisher(this.publisher)
                .author(this.author)
                .price(this.price)
                .stock(this.stock)
                .bookDescription(this.bookDescription)
                .status(this.status)
                .category(this.category)
                .photoImage(photoImage)
                .build();
    }

}
