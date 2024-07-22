package com.sparta.bookflex.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bookflex.domain.user.entity.QUser;
import com.sparta.bookflex.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryQueryImpl implements UserRepositoryQuery{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<User> findByUserName(String username) {
        QUser quser = QUser.user;
        return Optional.ofNullable(jpaQueryFactory
            .select(quser)
            .from(quser)
            .where(quser.username.eq(username))
            .fetchOne());
    }
}
