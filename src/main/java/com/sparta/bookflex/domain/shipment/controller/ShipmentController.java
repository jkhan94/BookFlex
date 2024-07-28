package com.sparta.bookflex.domain.shipment.controller;


import com.sparta.bookflex.common.aop.Envelop;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.shipment.dto.ShipmentReqDto;
import com.sparta.bookflex.domain.shipment.dto.ShipmentResDto;
import com.sparta.bookflex.domain.shipment.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @Envelop("배송정보 조회")
    @GetMapping()
    public ResponseEntity<List<ShipmentResDto>> getShipment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ShipmentReqDto reqDto){

        List<ShipmentResDto> shipmentResDtoList = shipmentService.getShipment(reqDto);
        return ResponseEntity.ok().body(shipmentResDtoList);
    }

    @Envelop("배송상태 조회")
    @GetMapping("/status")
    public ResponseEntity<ShipmentResDto> getStatus(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ShipmentReqDto reqDto){

        ShipmentResDto shipmentResDtoList = shipmentService.getStatus(reqDto);
        return ResponseEntity.ok().body(shipmentResDtoList);
    }
}
