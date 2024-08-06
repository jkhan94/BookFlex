package com.sparta.bookflex.domain.book.entity;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.exception.ErrorCode;
import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.basket.entity.BasketItem;
import com.sparta.bookflex.domain.book.dto.BookRequestDto;
import com.sparta.bookflex.domain.book.dto.BookResponseDto;
import com.sparta.bookflex.domain.category.enums.Category;
import com.sparta.bookflex.domain.orderbook.entity.OrderItem;
import com.sparta.bookflex.domain.photoimage.entity.PhotoImage;
import com.sparta.bookflex.domain.reveiw.entity.Review;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.wish.entity.Wish;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private String bookDescription;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category mainCategory;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category subCategory;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BasketItem> baskeItemtList = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Wish> wishList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderItem> orderItemList = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<Sale> saleList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "photoimage_id")
    private PhotoImage photoImage;

    @Builder
    public Book(String bookName,
                String publisher,
                String author,
                BigDecimal price,
                int stock,
                String bookDescription,
                BookStatus status,
                Category mainCategory,
                Category subCategory,
                PhotoImage photoImage) {

        this.bookName = bookName;
        this.publisher = publisher;
        this.author = author;
        this.price = price;
        this.stock = stock;
        this.bookDescription = bookDescription;
        this.status = status;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.photoImage = photoImage;

    }

    public BookResponseDto toResponseDto(String photoImageUrl) {

        return BookResponseDto.builder()
                .bookId(this.id)
                .bookName(this.bookName)
                .author(this.author)
                .publisher(this.publisher)
                .price(this.price)
                .stock(this.stock)
                .bookDescription(this.bookDescription)
                .status(this.status.getStatusValue())
                .mainCategoryName(this.mainCategory.getCategoryName())
                .subCategoryName(this.subCategory.getCategoryName())
                .photoImagePath(photoImageUrl)
                .avgStar(Math.round(this.reviewList.stream().mapToInt(Review::getStar).average().orElse(0)))
                .createdAt(this.createdAt)
                .modifiedAt(this.modifiedAt)
                .build();
    }

    public void update(BookRequestDto bookRequestDto) {

        this.bookName = bookRequestDto.getBookName();
        this.publisher = bookRequestDto.getPublisher();
        this.author = bookRequestDto.getAuthor();
        this.price = bookRequestDto.getPrice();
        this.stock = bookRequestDto.getStock();
        this.bookDescription = bookRequestDto.getBookDescription();
        this.status = bookRequestDto.getStock() > 1 ? BookStatus.ONSALE : BookStatus.SOLDOUT;
        this.mainCategory =Category.of(bookRequestDto.getMainCategory());
        this.subCategory = Category.of(bookRequestDto.getSubCategory());

    }

    public void decreaseStock(int quantity) {

        if (this.stock >= quantity) {
            this.stock -= quantity;
        } else {
            throw new BusinessException(ErrorCode.CANNOT_EXCEED,this.bookName);
        }

        checkStock();
    }

    public void increaseStock(int quantity) {

        this.stock += quantity;

        checkStock();
    }

    public void checkStock() {
        if (this.stock < 1) {
            this.status = BookStatus.SOLDOUT;
        } else {
            this.status = BookStatus.ONSALE;
        }
    }

}


