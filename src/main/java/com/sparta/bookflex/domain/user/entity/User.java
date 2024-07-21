package com.sparta.bookflex.domain.user.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.basket.entity.Basket;
import com.sparta.bookflex.domain.qna.entity.Qna;
import com.sparta.bookflex.domain.user.enums.UserGrade;
import com.sparta.bookflex.domain.user.enums.UserState;
import com.sparta.bookflex.domain.wish.entity.Wish;
import com.sparta.bookflex.domain.order.entity.Order;
import com.sparta.bookflex.domain.reveiw.entity.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column
    private String nickname;

    @Column
    private LocalDateTime birthDay;

    @Column(nullable = false)
    @Enumerated
    private UserGrade grade;

    @Column(nullable = false)
    @Enumerated
    private UserState state;

    @Column
    private String refreshToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Wish wish;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Basket basket;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Qna> qnaList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orderList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewList;

    @Builder
    public User(String username, String password, String email, String name, String nickname, String phoneNumber, String address, LocalDateTime birthDay, UserGrade grade, UserState state) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthDay = birthDay;
        this.grade = grade;
        this.state = state;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void deleteUser(){
        this.state = UserState.DELETE;
    }
}
