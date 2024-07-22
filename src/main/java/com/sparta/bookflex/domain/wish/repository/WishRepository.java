package com.sparta.bookflex.domain.wish.repository;

import com.sparta.bookflex.domain.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findAllByUserId(Long userId);
}
