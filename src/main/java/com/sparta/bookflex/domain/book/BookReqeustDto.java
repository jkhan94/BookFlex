package com.sparta.bookflex.domain.book;

import com.sparta.bookflex.domain.category.entity.Category;
import com.sparta.bookflex.domain.photoimage.entity.PhotoImage;
import lombok.Getter;
import lombok.Setter;

import java.awt.print.Book;

@Getter
@Setter
public class BookReqeustDto {

   private String bookName;
   private String publihser;
   private String author;
   private int price;
   private int stock;
   private String bookDescription;
   private String status;
   private Category category;
   private PhotoImage photoImage;

    public Book toEntity() {
        return Book.buil
    }
}
