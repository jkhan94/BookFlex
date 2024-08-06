package com.sparta.bookflex.domain.payment.repository;

import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.payment.entity.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {


    Payment findByPayToken(String payToken);


    Payment findByOrderBook(OrderBook orderBook);
}
