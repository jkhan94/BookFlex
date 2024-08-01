package com.sparta.bookflex.domain.user.repository;

import com.sparta.bookflex.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepositoryQuery {
    Optional<User> findByUserName(String username);
    Page<User> getUsers(String username, Pageable pageable);
}
