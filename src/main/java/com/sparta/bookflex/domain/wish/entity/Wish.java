package com.sparta.bookflex.domain.wish.entity;

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
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "wish", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<WishBook> wishBookList = new ArrayList<>();

    @Builder
    public Wish(User user) {
        this.user = user;
    }

}
