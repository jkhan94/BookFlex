package com.sparta.bookflex.domain.wish.repository;

import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.wish.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Page<Wish> findAllByUserId(Long userId, Pageable pageable);

    boolean existsByUserAndBook(User user, Book book);
}
