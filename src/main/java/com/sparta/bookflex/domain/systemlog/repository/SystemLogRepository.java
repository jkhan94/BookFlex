package com.sparta.bookflex.domain.systemlog.repository;

import com.sparta.bookflex.domain.systemlog.entity.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {
}
