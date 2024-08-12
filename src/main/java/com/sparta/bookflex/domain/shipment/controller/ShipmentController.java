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

/**
 * 배송정보 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 이 컨트롤러는 특정 유저의 배송정보 반환, 전체 배송정보 반환 등의 작업을 담당합니다.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;


    /**
     * 모든 배송 정보를 페이지네이션과 정렬 조건에 따라 조회합니다.
     *
     * @param page  조회할 페이지 번호
     * @param size  페이지 당 항목 수
     * @param isAsc 오름차순 정렬 여부 (true: 오름차순, false: 내림차순)
     * @return 조회된 배송 정보를 담은 TotalShipmentResDto 객체를 반환합니다.
     */
    @GetMapping()
    public ResponseEntity<TotalShipmentResDto> getAllShipmentInfo(@RequestParam("page") int page,
                                                                  @RequestParam("size") int size,
                                                                  @RequestParam("isAsc") boolean isAsc) {
        TotalShipmentResDto resDtoList = shipmentService.getAllShipmentInfo(page, size, isAsc);
        return ResponseEntity.ok().body(resDtoList);
    }


    /**
     * 현재 인증된 사용자의 배송 정보를 페이지네이션과 정렬 조건에 따라 조회합니다.
     *
     * @param page        조회할 페이지 번호
     * @param size        페이지 당 항목 수
     * @param isAsc       오름차순 정렬 여부 (true: 오름차순, false: 내림차순)
     * @param userDetails 현재 인증된 사용자 정보를 포함한 객체
     * @return 조회된 사용자별 배송 정보를 담은 TotalShipmentResDto 객체를 반환합니다.
     */
    @GetMapping("/users")
    public ResponseEntity<TotalShipmentResDto> getUserShipmentInfo(@RequestParam("page") int page,
                                                                   @RequestParam("size") int size,
                                                                   @RequestParam("isAsc") boolean isAsc,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TotalShipmentResDto resDtoList = shipmentService.getUserShipmentInfo(page, size, isAsc, userDetails.getUser());
        return ResponseEntity.ok().body(resDtoList);
    }
}

