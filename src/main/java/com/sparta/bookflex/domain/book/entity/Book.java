package com.sparta.bookflex.domain.book.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.basket.entity.BasketBook;
import com.sparta.bookflex.domain.book.dto.BookRequestDto;
import com.sparta.bookflex.domain.book.dto.BookResponseDto;
import com.sparta.bookflex.domain.category.entity.Category;
import com.sparta.bookflex.domain.photoimage.entity.PhotoImage;
import com.sparta.bookflex.domain.reveiw.entity.Review;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.wish.entity.WishBook;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Book extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bookName;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private String author;

    @Column
    private int price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private String bookDescription;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "book")
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BasketBook> basketBookList = new ArrayList<>();

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<WishBook> WishBookList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<Sale> saleList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "photoimage_id")
    private PhotoImage photoImage;

    @Builder
    public Book(String bookName, String publisher, String author, int price, int stock, String bookDescription, String status, Category category, PhotoImage photoImage) {
        this.bookName = bookName;
        this.publisher = publisher;
        this.author = author;
        this.price = price;
        this.stock = stock;
        this.bookDescription = bookDescription;
        this.status = status;
        this.category = category;
        this.photoImage = photoImage;

    }

    public BookResponseDto toResponseDto() {
        return BookResponseDto.builder()
                .bookId(this.id)
                .bookName(this.bookName)
                .author(this.author)
                .price(this.price)
                .stock(this.stock)
                .bookDescription(this.bookDescription)
                .status(this.status)
                .photoImagePath(this.photoImage.getFilePath())
                .createdAt(this.createdAt)
                .modifiedAt(this.modifiedAt)
                .build();
    }

    public void update(BookRequestDto bookRequestDto) {
        this.bookName = bookRequestDto.getBookName();
        this.author = bookRequestDto.getAuthor();
        this.price = bookRequestDto.getPrice();
        this.stock = bookRequestDto.getStock();
        this.bookDescription = bookRequestDto.getBookDescription();
        this.status = bookRequestDto.getStatus();
    }

    public void decreaseStock (int number) {
        this.stock-=number;
    }
}


