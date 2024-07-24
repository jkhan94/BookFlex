package com.sparta.bookflex.domain.sale.repository;

import com.sparta.bookflex.domain.sale.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long>, SaleQRepository {
}
