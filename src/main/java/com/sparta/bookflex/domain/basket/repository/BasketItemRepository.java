package com.sparta.bookflex.domain.basket.repository;

import com.sparta.bookflex.domain.basket.entity.BasketItem;
import com.sparta.bookflex.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {
    boolean existsByBookIdAndBasketId(Long bookId, Long basketId);

    Page<BasketItem> findAllByBasketUserId(long id, Pageable pageable);

    Page<BasketItem> findAllByBasketId(Long basketId, Pageable pageable);

    Optional<BasketItem> findByBookId(Long bookId);
}
