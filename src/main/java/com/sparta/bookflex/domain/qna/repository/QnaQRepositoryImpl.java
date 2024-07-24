package com.sparta.bookflex.domain.qna.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bookflex.domain.qna.entity.Qna;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.sparta.bookflex.domain.qna.entity.QQna.qna;

@RequiredArgsConstructor
public class QnaQRepositoryImpl implements QnaQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Qna> findAllByUserId(long userId, Pageable pageable) {
        List<Qna> result = queryFactory.select(qna)
                .from(qna)
                .where(qna.user.id.eq(userId))
                .orderBy(qna.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Qna> count = queryFactory.select(qna)
                .from(qna)
                .where(qna.user.id.eq(userId))
                .orderBy(qna.createdAt.desc())
                .fetch();

        return new PageImpl<>(result, pageable, count.size());
    }
}
