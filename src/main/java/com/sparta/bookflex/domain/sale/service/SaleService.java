package com.sparta.bookflex.domain.sale.service;

import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.service.BookService;
import com.sparta.bookflex.domain.sale.Enum.SaleState;
import com.sparta.bookflex.domain.orderbook.dto.OrderRequestDto;
import com.sparta.bookflex.domain.sale.dto.SaleRequestDto;
import com.sparta.bookflex.domain.sale.dto.SaleResponseDto;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.sale.repository.SaleRepository;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.service.AuthService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final AuthService authService;
    private final BookService bookService;


    public SaleService(SaleRepository saleRepository, AuthService authService, BookService bookService) {
        this.saleRepository = saleRepository;
        this.authService = authService;
        this.bookService = bookService;
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
                .status(SaleState.PENDING_PAYMENT)
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
                    .status(SaleState.PENDING_PAYMENT)
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
                .price(sale.getBook().getPrice())
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
                        .price(sale.getBook().getPrice())
                        .quantity(sale.getQuantity())
                        .status(sale.getStatus().getDesscription())
                        .createdAt(sale.getCreatedAt())
                        .build()
        );

    }
}
