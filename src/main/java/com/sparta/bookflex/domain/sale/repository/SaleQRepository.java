package com.sparta.bookflex.domain.sale.repository;

import com.querydsl.core.Tuple;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface SaleQRepository {
    Page<Sale> findAllSalesByUser(User user, Pageable pageable);

    Page<Tuple> findSaleByBookName(String bookName, LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<Tuple> findSaleByCategory(String CategoryName, LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<Tuple> findSales(String username, String status, LocalDate startDate, LocalDate endDate, Pageable pageable);


}
