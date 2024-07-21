package com.sparta.bookflex.domain.user.repository;

import com.sparta.bookflex.domain.user.entity.User;

import java.util.Optional;

public interface UserRepositoryQuery {
    Optional<User> findByUserName(String username);
}
