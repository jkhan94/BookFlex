package com.sparta.bookflex.domain.user.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.basket.entity.Basket;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.qna.entity.Qna;
import com.sparta.bookflex.domain.reveiw.entity.Review;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.shipment.entity.Shipment;
import com.sparta.bookflex.domain.systemlog.entity.CopyOfSystemLog;
import com.sparta.bookflex.domain.user.dto.ProfileReqDto;
import com.sparta.bookflex.domain.user.dto.ProfileResDto;
import com.sparta.bookflex.domain.user.enums.UserRole;
import com.sparta.bookflex.domain.user.enums.UserGrade;
import com.sparta.bookflex.domain.user.enums.UserState;
import com.sparta.bookflex.domain.usercoupon.entity.UserCoupon;
import com.sparta.bookflex.domain.wish.entity.Wish;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
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
    private LocalDate birthDay;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserGrade grade;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserState state;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole auth;

    @Column()
    private String refreshToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Basket basket;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private com.sparta.bookflex.domain.user.entity.UserRole userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Qna> qnaList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sale> saleList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CopyOfSystemLog> copyOfSystemLogList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Shipment> shipmentList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderBook> orderBookList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCoupon> userCouponList;

    @Builder
    public User(String username, String password, String email, String name, String nickname, String phoneNumber, String address, LocalDate birthDay, UserGrade grade, UserState state, UserRole auth) {
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
        this.auth = auth;
    }

    public static ProfileResDto of(User user) {
        return ProfileResDto.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .address(user.getAddress())
            .phoneNumber(user.getPhoneNumber())
            .nickname(user.getNickname())
            .grade(user.getGrade())
            .createdAt(user.getCreatedAt())
            .build();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateState(UserState state) {
        this.state = state;
    }

    public void updateGrade(UserGrade grade){
        this.grade = grade;
    }

    public void updateProfile(String password, String nickname, String phoneNumber, String address) {
        if (!password.isEmpty()) {
            this.password = password;
        }
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void createCart() {
        this.basket = Basket.builder().user(this).build();
    }
}
