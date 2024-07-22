package com.sparta.bookflex.domain.sale.controller;

import com.sparta.bookflex.common.dto.CommonDto;
import com.sparta.bookflex.domain.sale.dto.SaleCreateReqDto;
import com.sparta.bookflex.domain.sale.dto.SaleResDto;
import com.sparta.bookflex.domain.sale.service.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/sales")
public class SaleController {

    private SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping()
    public ResponseEntity<CommonDto<Void>> createSale(
            @RequestBody SaleCreateReqDto saleCreateReqDto
            //,@AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        Long userId = 1L;
        saleService.createSale(saleCreateReqDto, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonDto<>(HttpStatus.CREATED.value(), "주문이 완료되었습니다.", null));
    }

    @GetMapping("/{saleId")
    public ResponseEntity<CommonDto<SaleResDto>> getSale(
            @PathVariable Long saleId
            //,@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = 1L;
        SaleResDto saleResDto = saleService.getSale(saleId, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "주문 내역을 조회했습니다.", saleResDto));
    }

    @DeleteMapping("/{saleId}")
    public ResponseEntity<CommonDto<Void>> deleteSale(
            @PathVariable Long saleId
            //,@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = 1L;
        saleService.deleteSale(saleId, userId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT).body(null);
    }
}
