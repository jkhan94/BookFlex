package com.sparta.bookflex.domain.shipment.controller;

import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.shipment.dto.TotalShipmentResDto;
import com.sparta.bookflex.domain.shipment.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

//    @PostMapping()
//    public ResponseEntity<ShipmentResDto> getShipment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ShipmentReqDto reqDto){
//
//        ShipmentResDto shipmentResDto = shipmentService.getShipment(reqDto, userDetails.getUser());
//        return ResponseEntity.ok().body(shipmentResDto);
//    }

    //    @GetMapping("/status")
//    public ResponseEntity<ShipmentResDto> getStatus(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ShipmentReqDto reqDto){
//
//        ShipmentResDto shipmentResDtoList = shipmentService.getShipmentStatus(reqDto.getTrackingNumber(), reqDto.getCarrier());
//        return ResponseEntity.ok().body(shipmentResDtoList);
//    }
    @GetMapping()
    public ResponseEntity<TotalShipmentResDto> getAllShipmentInfo(@RequestParam("page") int page,
                                                                  @RequestParam("size") int size,
                                                                  @RequestParam("isAsc") boolean isAsc) {
        TotalShipmentResDto resDtoList = shipmentService.getAllShipmentInfo(page, size, isAsc);
        return ResponseEntity.ok().body(resDtoList);
    }

    @GetMapping("/users")
    public ResponseEntity<TotalShipmentResDto> getUserShipmentInfo(@RequestParam("page") int page,
                                                                   @RequestParam("size") int size,
                                                                   @RequestParam("isAsc") boolean isAsc,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TotalShipmentResDto resDtoList = shipmentService.getUserShipmentInfo(page, size, isAsc, userDetails.getUser());
        return ResponseEntity.ok().body(resDtoList);
    }
}

