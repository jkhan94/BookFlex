package com.sparta.bookflex.domain.qna.repository;

import com.sparta.bookflex.domain.qna.entity.Qna;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long>, QnaQRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT q FROM Qna q WHERE q.id = :id")
    Optional<Qna> findByIdWithLock(@Param("id") long qnaId);
}
