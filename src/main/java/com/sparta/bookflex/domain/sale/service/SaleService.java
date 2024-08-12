package com.sparta.bookflex.domain.sale.service;

import com.querydsl.core.Tuple;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.service.BookService;
import com.sparta.bookflex.domain.category.enums.Category;
import com.sparta.bookflex.domain.orderbook.dto.OrderRequestDto;
import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.orderbook.repository.OrderBookRepository;
import com.sparta.bookflex.domain.photoimage.service.PhotoImageService;
import com.sparta.bookflex.domain.sale.dto.*;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.sale.repository.SaleQRepositoryImpl;
import com.sparta.bookflex.domain.sale.repository.SaleRepository;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.auth.service.AuthService;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final AuthService authService;
    private final BookService bookService;
    private final OrderBookRepository orderBookRepository;
    private final SaleQRepositoryImpl saleQRepositoryImpl;
    private final PhotoImageService photoImageService;

    @Autowired
    public SaleService(SaleRepository saleRepository,
                       AuthService authService,
                       BookService bookService,
                       OrderBookRepository orderBookRepository,
                       SaleQRepositoryImpl saleQRepositoryImpl,
                       PhotoImageService photoImageService) {
        this.saleRepository = saleRepository;
        this.authService = authService;
        this.bookService = bookService;
        this.orderBookRepository = orderBookRepository;
        this.saleQRepositoryImpl = saleQRepositoryImpl;
        this.photoImageService = photoImageService;
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

        Page<SaleVolumeRowDto> saleVolumeRowDtos = saleQRepositoryImpl.findSaleByBookName(bookName, startDate, endDate, pageable)
                .map(tuple -> SaleVolumeRowDto
                        .builder()
                        .bookName(tuple.get(0, String.class))
                        .categoryName(tuple.get(1, Category.class))
                        .publisher(tuple.get(2, String.class))
                        .author(tuple.get(3, String.class))
                        .price(tuple.get(4, BigDecimal.class).setScale(0, RoundingMode.FLOOR))
                        .quantity(tuple.get(5, Integer.class))
                        .totalPrice(tuple.get(6, BigDecimal.class).setScale(0, RoundingMode.FLOOR))
                        .build());


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

        Page<SaleVolumeRowDto> saleVolumeRowDtos = saleQRepositoryImpl.findSaleByCategory(categoryName, startDate, endDate, pageable)
                .map(tuple -> SaleVolumeRowDto
                        .builder()
                        .categoryName(tuple.get(0, Category.class))
                        .quantity(tuple.get(1, Integer.class))
                        .totalPrice(tuple.get(2, BigDecimal.class).setScale(0, RoundingMode.FLOOR))
                        .build());


        TotalSaleVolumeDto totalSaleVolume = TotalSaleVolumeDto.builder()
                .totalSaleVolume(saleVolumeRowDtos.stream()
                        .map(saleVolume -> saleVolume.getTotalPrice()).reduce(BigDecimal.ZERO, BigDecimal::add)).build();

        return SaleVolumeResponseDto.builder()
                .firstData(saleVolumeRowDtos)
                .secondData(totalSaleVolume).build();

    }

    public Page<SaleListDto> getAllSales(int page, int size, boolean isAsc, String sortBy, String status, String username, LocalDate startDate, LocalDate endDate) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<SaleListDto> saleListDtos = saleQRepositoryImpl.findSales(username, status, startDate, endDate, pageable)
                .map(tuple -> SaleListDto
                        .builder()
                        .createdAt(tuple.get(0, LocalDateTime.class))
                        .saleId(tuple.get(1, Long.class))
                        .username(tuple.get(2, String.class))
                        .bookName(bookService.getBookById(tuple.get(3, Long.class)).getBookName())
                        .orderState(Objects.requireNonNull(tuple.get(4, OrderState.class)).getDesscription())
                        .totalAmount(tuple.get(5, BigDecimal.class).setScale(0, RoundingMode.FLOOR))
                        .quantity((tuple.get(6, Integer.class)))
                        .price(tuple.get(7, BigDecimal.class).setScale(0, RoundingMode.FLOOR))
                        .build());

        return saleListDtos;
    }


}
