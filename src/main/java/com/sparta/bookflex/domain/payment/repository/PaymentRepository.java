package com.sparta.bookflex.domain.payment.repository;

import com.sparta.bookflex.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
