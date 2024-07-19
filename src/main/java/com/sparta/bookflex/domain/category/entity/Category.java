package com.sparta.bookflex.domain.category.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.book.entity.Book;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Category extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> bookList;

    @Builder
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
