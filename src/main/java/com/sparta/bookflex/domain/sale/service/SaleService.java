package com.sparta.bookflex.domain.sale.service;

import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.service.BookService;
import com.sparta.bookflex.domain.category.enums.Category;
import com.sparta.bookflex.domain.orderbook.dto.OrderRequestDto;
import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.orderbook.repository.OrderBookRepository;
import com.sparta.bookflex.domain.sale.dto.*;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.sale.repository.SaleQRepositoryImpl;
import com.sparta.bookflex.domain.sale.repository.SaleRepository;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final AuthService authService;
    private final BookService bookService;
    private final OrderBookRepository orderBookRepository;
    private final SaleQRepositoryImpl saleQRepositoryImpl;

    @Autowired
    public SaleService(SaleRepository saleRepository,
                       AuthService authService,
                       BookService bookService,
                       OrderBookRepository orderBookRepository, SaleQRepositoryImpl saleQRepositoryImpl) {
        this.saleRepository = saleRepository;
        this.authService = authService;
        this.bookService = bookService;
        this.orderBookRepository = orderBookRepository;
        this.saleQRepositoryImpl = saleQRepositoryImpl;
    }

    private Book getBook(Long bookId) {
        return bookService.getBookByBookId(bookId);
    }

    private Sale getSale(Long saleId) {
        return saleRepository.findById(saleId).orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
    }

    @Transactional
    public void createSale(SaleRequestDto saleCreateReqDto, User user) {

        Book book = getBook(saleCreateReqDto.getBookId());
        Sale sale = Sale.builder()
                .quantity(saleCreateReqDto.getQuantity())
                .status(OrderState.PENDING_PAYMENT)
                .book(book)
                .user(user)
                .build();
        saleRepository.save(sale);
    }

    @Transactional
    public void createMultipleSales(OrderRequestDto orderRequestDto, User user) {

        for (OrderRequestDto.OrderItemDto orderItemDto : orderRequestDto.getItems()) {
            Book book = getBook(orderItemDto.getBookId());
            Sale sale = Sale.builder()
                    .quantity(orderItemDto.getQuantity())
                    .status(OrderState.PENDING_PAYMENT)
                    .book(book)
                    .user(user)
                    .build();
            saleRepository.save(sale);
        }
    }

    public SaleResponseDto findSaleById(Long saleId, User user) {
        Sale sale = getSale(saleId);
        return SaleResponseDto.builder()
                .saleId(sale.getId())
                .bookName(sale.getBook().getBookName())

                .quantity(sale.getQuantity())
                .status(sale.getStatus().getDesscription())
                .createdAt(sale.getCreatedAt())
                .build();
    }


    public Page<SaleResponseDto> getAllSalesForUser(User user, Pageable pageable) {
        return saleRepository.findAllSalesByUser(user, pageable).map(
                sale -> SaleResponseDto.builder()
                        .saleId(sale.getId())
                        .bookName(sale.getBook().getBookName())

                        .quantity(sale.getQuantity())
                        .status(sale.getStatus().getDesscription())
                        .createdAt(sale.getCreatedAt())
                        .build()
        );

    }

    @Transactional
    public void createSaleAndOrder(SaleRequestDto saleCreateReqDto, User user) {
        Book book = getBook(saleCreateReqDto.getBookId());

        Sale sale = Sale.builder()
                .quantity(saleCreateReqDto.getQuantity())
                .status(OrderState.PENDING_PAYMENT)
                .book(book)
                .user(user)

                .build();

        saleRepository.save(sale);
    }

//    public void updateSaleStatus(Long saleId, SaleState saleState, User user) {
//        Sale sale = getSale(saleId);
//        sale.updateStatus(saleState);
//        saleRepository.save(sale);
//    }

    public SaleVolumeResponseDto getSaleVoulmesByBookName(int page, int size, boolean isAsc, String sortBy, String bookName, LocalDate startDate, LocalDate endDate) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        List<SaleVolumeRowDto> saleVolumeRowDtos = saleQRepositoryImpl.findSaleByBookName(bookName, startDate, endDate, pageable)
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

        List<SaleVolumeRowDto> saleVolumeRowDtos = saleQRepositoryImpl.findSaleByCategory(categoryName, startDate, endDate, pageable)
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
