package com.sparta.bookflex.domain.salevolume.repository;

import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface SaleVolumeCustomRepository {

    Page<Tuple> findSaleByBookName(String bookName, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Tuple> findSaleByCategory(String CategoryName, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
