package com.sparta.bookflex.domain.basket.repository;

import com.sparta.bookflex.domain.basket.entity.Basket;
import com.sparta.bookflex.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {


    Optional<Basket> findByUser(User user);

    boolean existsByIdAndUser(Long basketId, User user);

    Optional<Basket> findByUserId(long userId);
}
