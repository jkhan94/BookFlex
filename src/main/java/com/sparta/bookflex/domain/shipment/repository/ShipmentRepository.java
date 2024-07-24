package com.sparta.bookflex.domain.shipment.repository;

import com.sparta.bookflex.domain.shipment.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
