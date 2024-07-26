package com.sparta.bookflex.domain.salevolume.service;

import com.sparta.bookflex.domain.book.repository.BookRepository;
import com.sparta.bookflex.domain.category.enums.Category;
import com.sparta.bookflex.domain.sale.repository.SaleRepository;
import com.sparta.bookflex.domain.salevolume.dto.SaleVolumeResponseDto;
import com.sparta.bookflex.domain.salevolume.dto.SaleVolumeRowDto;
import com.sparta.bookflex.domain.salevolume.dto.TotalSaleVolumeDto;
import com.sparta.bookflex.domain.salevolume.repository.SaleVolumeCustomRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesVolumeService {
    private final SaleRepository saleRepsitory;
    private final SaleVolumeCustomRepositoryImpl saleVolumeCustomRepositoryimpl;
    private final BookRepository bookRepository;

    public SaleVolumeResponseDto getSaleVoulmesByBookName(int page, int size, boolean isAsc, String sortBy, String bookName, LocalDate startDate, LocalDate endDate) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        List<SaleVolumeRowDto> saleVolumeRowDtos = saleVolumeCustomRepositoryimpl.findSaleByBookName(bookName, startDate, endDate, pageable)
                .stream()
                .map(tuple -> SaleVolumeRowDto
                        .builder()
                        .bookName(tuple.get(0, String.class))
                        .categoryName(tuple.get(1, Category.class))
                        .publisher(tuple.get(2, String.class))
                        .author(tuple.get(3, String.class))
                        .price(tuple.get(4, BigDecimal.class).setScale(0, RoundingMode.FLOOR))
                        .quantity(tuple.get(5, Integer.class))
                        .totalPrice(tuple.get(6, BigDecimal.class).setScale(0, RoundingMode.FLOOR))
                        .build()).toList();


        TotalSaleVolumeDto totalSaleVolume = TotalSaleVolumeDto.builder()
                .totalSaleVolume(saleVolumeRowDtos.stream()
                        .map(saleVolume -> saleVolume.getTotalPrice()).reduce(BigDecimal.ZERO, BigDecimal::add)).build();

        return SaleVolumeResponseDto.builder()
                .firstData(saleVolumeRowDtos)
                .secondData(totalSaleVolume).build();

    }

    public SaleVolumeResponseDto getSaleVolumesByCategory(int page, int size, boolean isAsc, String sortBy, String categoryName, LocalDate startDate, LocalDate endDate) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        List<SaleVolumeRowDto> saleVolumeRowDtos = saleVolumeCustomRepositoryimpl.findSaleByCategory(categoryName, startDate, endDate, pageable)
                .stream()
                .map(tuple -> SaleVolumeRowDto
                        .builder()
                        .categoryName(tuple.get(0, Category.class))
                        .quantity(tuple.get(1, Integer.class))
                        .totalPrice(tuple.get(2, BigDecimal.class).setScale(0, RoundingMode.FLOOR))
                        .build()).toList();

        TotalSaleVolumeDto totalSaleVolume = TotalSaleVolumeDto.builder()
                .totalSaleVolume(saleVolumeRowDtos.stream()
                        .map(saleVolume -> saleVolume.getTotalPrice()).reduce(BigDecimal.ZERO, BigDecimal::add)).build();

        return SaleVolumeResponseDto.builder()
                .firstData(saleVolumeRowDtos)
                .secondData(totalSaleVolume).build();

    }
}
