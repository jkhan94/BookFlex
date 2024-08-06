package com.sparta.bookflex.domain.user.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bookflex.domain.user.entity.QUser;
import com.sparta.bookflex.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.sparta.bookflex.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryQueryImpl implements UserRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<User> findByUserName(String username) {
        QUser quser = user;
        return Optional.ofNullable(jpaQueryFactory
                .select(quser)
                .from(quser)
                .where(quser.username.eq(username))
                .fetchOne());
    }

    public Page<User> getUsers(String username, Pageable pageable) {
        List<User> result = jpaQueryFactory.select(user)
                .from(user)
                .where(eqUsername(username))
                .orderBy(user.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(user.count())
                .from(user)
                .where(eqUsername(username))
                .orderBy(user.id.asc())
                .fetchOne();

        return new PageImpl<>(result, pageable, count);
    }

    private BooleanExpression eqUsername(String username) {

        if (StringUtils.isEmpty(username)) {
            return null;
        }

        return user.username.eq(username);
    }


}
