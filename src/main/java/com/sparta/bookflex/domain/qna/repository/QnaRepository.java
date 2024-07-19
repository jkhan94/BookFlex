package com.sparta.bookflex.domain.qna.repository;

import com.sparta.bookflex.domain.qna.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<Qna, Long> {
}
