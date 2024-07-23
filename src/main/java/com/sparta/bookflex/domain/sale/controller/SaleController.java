package com.sparta.bookflex.domain.sale.controller;

import com.sparta.bookflex.common.dto.CommonDto;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.orderbook.dto.OrderRequestDto;
import com.sparta.bookflex.domain.sale.dto.SaleRequestDto;
import com.sparta.bookflex.domain.sale.dto.SaleResponseDto;
import com.sparta.bookflex.domain.sale.service.SaleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping()
    public ResponseEntity<CommonDto<Void>> createSale(
            @RequestBody SaleRequestDto saleCreateReqDto
            ,@AuthenticationPrincipal UserDetailsImpl userDetails) {

        saleService.createSale(saleCreateReqDto, userDetails.getUser());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonDto<>(HttpStatus.CREATED.value(), "주문이 완료되었습니다.", null));
    }

  /*  @PostMapping("/orderplace")
    public ResponseEntity<CommonDto<Void>> createMultipleSales(
            @RequestBody OrderRequestDto orderRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        saleService.createMultipleSales(orderRequestDto, userDetails.getUser());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonDto<>(HttpStatus.CREATED.value(), "주문이 완료되었습니다.", null));
    }*/

    @GetMapping("/{saleId}")
    public ResponseEntity<CommonDto<SaleResponseDto>> getSale(
            @PathVariable Long saleId
            ,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        SaleResponseDto saleResDto = saleService.findSaleById(saleId, userDetails.getUser());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "주문 내역을 조회했습니다.", saleResDto));
    }

    @GetMapping("")
    public ResponseEntity<CommonDto<Page<SaleResponseDto>>> getAllSalesForUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable) {
        Page<SaleResponseDto> saleResponseDtoList = saleService.getAllSalesForUser(userDetails.getUser(), pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "주문 목록을 조회했습니다.", saleResponseDtoList));
    }


}
