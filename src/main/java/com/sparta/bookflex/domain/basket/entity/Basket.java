package com.sparta.bookflex.domain.basket.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Basket extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "basket", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<BasketItem> basketItems = new ArrayList<>();

    @Builder
    public Basket(User user) {
        this.user = user;
    }

    public void addBasketItem(BasketItem basketItem) {
        basketItems.add(basketItem);
    }
}
