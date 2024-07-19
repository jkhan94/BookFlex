package com.sparta.bookflex.domain.photoimage.repository;

import com.sparta.bookflex.domain.photoimage.entity.PhotoImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoImageRepository extends JpaRepository<PhotoImage, Long> {
}
