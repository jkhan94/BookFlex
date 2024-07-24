package com.sparta.bookflex.domain.sale.repository;

import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SaleQRepository {
    Page<Sale> findAllSalesByUser(User user, Pageable pageable);
}
