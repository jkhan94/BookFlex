package com.sparta.bookflex.domain.user.repository;

import com.sparta.bookflex.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryQuery {

}
