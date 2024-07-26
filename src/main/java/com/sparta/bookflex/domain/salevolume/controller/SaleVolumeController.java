package com.sparta.bookflex.domain.salevolume.controller;

import com.sparta.bookflex.common.dto.CommonDto;
import com.sparta.bookflex.domain.salevolume.dto.SaleVolumeResponseDto;
import com.sparta.bookflex.domain.salevolume.service.SalesVolumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sales")
public class SaleVolumeController {

    private final SalesVolumeService salesVolumeService;

    @GetMapping("/byBookMame")
    public ResponseEntity<CommonDto<SaleVolumeResponseDto>> getSaleVoulmesByBookName(@RequestParam(name = "page") int page,
                                                                                     @RequestParam(name = "size") int size,
                                                                                     @RequestParam(name = "direction") boolean isAsc,
                                                                                     @RequestParam(name = "sortBy", required = false, defaultValue = "bookName") String sortBy,
                                                                                     @RequestParam(name = "bookName", required = false) String bookName,
                                                                                     @RequestParam(name = "startDate", required = false, defaultValue = "19000101") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate startDate,
                                                                                     @RequestParam(name = "endDate", required = false, defaultValue = "99991231") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate endDate) {


        SaleVolumeResponseDto saleVolumeResponseDto =  salesVolumeService.getSaleVoulmesByBookName(page, size, isAsc, sortBy, bookName, startDate, endDate);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "상품별 매출내역 조회가 완료되었습니다.", saleVolumeResponseDto));


    }

    @GetMapping("/byCategoryName")
    public ResponseEntity<CommonDto<SaleVolumeResponseDto>> getSaleVoulmesByCategory(@RequestParam(name = "page") int page,
                                                                                     @RequestParam(name = "size") int size,
                                                                                     @RequestParam(name = "direction") boolean isAsc,
                                                                                     @RequestParam(name = "sortBy", required = false, defaultValue = "bookName") String sortBy,
                                                                                     @RequestParam(name = "categoryName", required = false) String categoryName,
                                                                                     @RequestParam(name = "startDate", required = false, defaultValue = "19000101") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate startDate,
                                                                                     @RequestParam(name = "endDate", required = false, defaultValue = "99991231") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate endDate) {

        SaleVolumeResponseDto saleVolumeResponseDto =
                salesVolumeService.getSaleVolumesByCategory(page, size, isAsc, sortBy, categoryName, startDate, endDate);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "카테고리별 매출내역 조회가 완료되었습니다.", saleVolumeResponseDto));
    }

}
