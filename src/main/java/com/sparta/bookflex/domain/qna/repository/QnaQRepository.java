package com.sparta.bookflex.domain.qna.repository;

import com.sparta.bookflex.domain.qna.entity.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaQRepository {
    Page<Qna> findAllByUserId(long userId, Pageable pageable);
}
