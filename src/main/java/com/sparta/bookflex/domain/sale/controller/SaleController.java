package com.sparta.bookflex.domain.sale.controller;

import com.sparta.bookflex.common.dto.CommonDto;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.sale.dto.SaleListDto;
import com.sparta.bookflex.domain.sale.dto.SaleRequestDto;
import com.sparta.bookflex.domain.sale.dto.SaleResponseDto;
import com.sparta.bookflex.domain.sale.dto.SaleVolumeResponseDto;
import com.sparta.bookflex.domain.sale.service.SaleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping()
    public ResponseEntity<CommonDto<Void>> createSale(
            @RequestBody SaleRequestDto saleCreateReqDto
            , @AuthenticationPrincipal UserDetailsImpl userDetails) {

        saleService.createSaleAndOrder(saleCreateReqDto, userDetails.getUser());
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
            , @AuthenticationPrincipal UserDetailsImpl userDetails) {
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

    @GetMapping("/admin")
    public ResponseEntity<CommonDto<Page<SaleListDto>>> getAllSales(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                        @RequestParam(name = "size", defaultValue = "5") int size,
                                                                        @RequestParam(name = "direction", required = false, defaultValue = "true") boolean isAsc,
                                                                        @RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
                                                                        @RequestParam(name = "status", required = false) String status,
                                                                        @RequestParam(name = "username", required = false) String username,
                                                                        @RequestParam(name = "startDate", required = false, defaultValue = "19000101") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate startDate,
                                                                        @RequestParam(name = "endDate", required = false, defaultValue = "99991231") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate endDate) {

        Page<SaleListDto> saleResponseDtoList = saleService.getAllSales(page, size, isAsc, sortBy, status, username, startDate, endDate);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "주문 목록을 조회했습니다.", saleResponseDtoList));
    }


//    @PutMapping("/{saleId}/status")
//    public ResponseEntity<CommonDto<Void>> updateSaleStatus(
//            @PathVariable Long saleId,
//            @RequestBody SaleStateReqestDto saleStateReqDto,
//            @AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//        saleService.updateSaleStatus(saleId, saleStateReqDto.getSaleState(), userDetails.getUser());
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(new CommonDto<>(HttpStatus.OK.value(), "판매 상태가 변경되었습니다.", null));
//    }

    @GetMapping("/bybookname")
    public ResponseEntity<CommonDto<SaleVolumeResponseDto>> getSaleVoulmesByBookName(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                                     @RequestParam(name = "size", defaultValue = "5") int size,
                                                                                     @RequestParam(name = "direction", required = false, defaultValue = "true") boolean isAsc,
                                                                                     @RequestParam(name = "sortBy", required = false, defaultValue = "bookName") String sortBy,
                                                                                     @RequestParam(name = "bookName", required = false) String bookName,
                                                                                     @RequestParam(name = "startDate", required = false, defaultValue = "19000101") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate startDate,
                                                                                     @RequestParam(name = "endDate", required = false, defaultValue = "99991231") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate endDate) {


        SaleVolumeResponseDto saleVolumeResponseDto = saleService.getSaleVoulmesByBookName(page, size, isAsc, sortBy, bookName, startDate, endDate);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "상품별 매출내역 조회가 완료되었습니다.", saleVolumeResponseDto));


    }


    @GetMapping("/byCategoryName")
    public ResponseEntity<CommonDto<SaleVolumeResponseDto>> getSaleVoulmesByCategory(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                                     @RequestParam(name = "size", defaultValue = "5") int size,
                                                                                     @RequestParam(name = "direction") boolean isAsc,
                                                                                     @RequestParam(name = "sortBy", required = false, defaultValue = "bookName") String sortBy,
                                                                                     @RequestParam(name = "categoryName", required = false) String categoryName,
                                                                                     @RequestParam(name = "startDate", required = false, defaultValue = "19000101") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate startDate,
                                                                                     @RequestParam(name = "endDate", required = false, defaultValue = "99991231") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate endDate) {

        SaleVolumeResponseDto saleVolumeResponseDto =
                saleService.getSaleVolumesByCategory(page, size, isAsc, sortBy, categoryName, startDate, endDate);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "카테고리별 매출내역 조회가 완료되었습니다.", saleVolumeResponseDto));
    }


}
