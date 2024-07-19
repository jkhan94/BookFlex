package com.sparta.bookflex.domain.wish.repository;

import com.sparta.bookflex.domain.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {
}
