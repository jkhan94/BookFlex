package com.sparta.bookflex.domain.systemlog.repository;

import com.sparta.bookflex.domain.systemlog.entity.TraceOfUserLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraceOfUserLogRepository extends JpaRepository<TraceOfUserLog, Long> {
}
