package com.sparta.bookflex.domain.shipment.repository;

import com.sparta.bookflex.domain.shipment.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Page<Shipment> findAll(Pageable pageable);

    Page<Shipment> findAllByUserId(long userId, Pageable pageable);

    @Query("select count(s.user.id) from Shipment s where s.user.id = :userId")
    Long userShipInfoCount(@Param("userId") long userId);

    Optional<Shipment> findByOrderBookId(Long id);
}
