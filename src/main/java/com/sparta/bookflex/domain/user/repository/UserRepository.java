package com.sparta.bookflex.domain.user.repository;

import com.sparta.bookflex.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryQuery {

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByEmail(String kakaoEmail);


}
