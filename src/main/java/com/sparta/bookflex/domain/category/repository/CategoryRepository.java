package com.sparta.bookflex.domain.category.repository;

import com.sparta.bookflex.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryQRepository {
    Optional<Category> findByCategoryName(String categoryName);
}
