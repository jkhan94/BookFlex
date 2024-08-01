package com.sparta.bookflex.domain.shipment.controller;


import com.sparta.bookflex.common.aop.Envelop;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.shipment.dto.ShipmentReqDto;
import com.sparta.bookflex.domain.shipment.dto.ShipmentResDto;
import com.sparta.bookflex.domain.shipment.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping()
    public ResponseEntity<ShipmentResDto> getShipment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ShipmentReqDto reqDto){

        ShipmentResDto shipmentResDto = shipmentService.getShipment(reqDto);
        return ResponseEntity.ok().body(shipmentResDto);
    }

    @Envelop("배송상태 조회")
    @GetMapping("/status")
    public ResponseEntity<ShipmentResDto> getStatus(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ShipmentReqDto reqDto){

        ShipmentResDto shipmentResDtoList = shipmentService.getStatus(reqDto);
        return ResponseEntity.ok().body(shipmentResDtoList);
    }
}
