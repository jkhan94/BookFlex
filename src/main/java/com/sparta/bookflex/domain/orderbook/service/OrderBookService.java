package com.sparta.bookflex.domain.orderbook.service;

import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.service.BookService;
import com.sparta.bookflex.domain.orderbook.dto.OrderResponsDto;
import com.sparta.bookflex.domain.orderbook.dto.OrderStatusRequestDto;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.orderbook.repository.OrderBookRepository;
import com.sparta.bookflex.domain.sale.Enum.SaleState;
import com.sparta.bookflex.domain.orderbook.dto.OrderRequestDto;
import com.sparta.bookflex.domain.sale.dto.SaleResponseDto;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.sale.repository.SaleRepository;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderBookService {

    private SaleRepository saleRepository;
    private OrderBookRepository orderBookRepository;
    private final AuthService authService;
    private final BookService bookService;

    @Autowired
    public OrderBookService(SaleRepository saleRepository,
                            OrderBookRepository orderBookRepository,
                            AuthService authService, BookService bookService) {
        this.saleRepository = saleRepository;
        this.orderBookRepository = orderBookRepository;
        this.authService = authService;
        this.bookService = bookService;
    }

    private Book getBook(Long bookId) {
        return bookService.getBookByBookId(bookId);
    }

    private OrderBook getOrderBook(Long orderId) {
        return orderBookRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 내역이 없습니다."));

    }

    @Transactional
    public OrderBook createOrder(OrderRequestDto orderRequestDto, User user) {
        int total = 0;
        List<Sale> sales = new ArrayList<>();

        for (OrderRequestDto.OrderItemDto item : orderRequestDto.getItems()) {
            Book book = getBook(item.getBookId());
            int itemTotal = item.getQuantity() * book.getPrice();
            total += itemTotal;

            Sale sale = Sale.builder()
                    .status(SaleState.PENDING_PAYMENT)
                    .quantity(item.getQuantity())
                    .book(book)
                    .user(user)
                    .orderBook(null)
                    .build();
            sales.add(sale);
        }

        OrderBook orderBook = OrderBook.builder()
                .status(SaleState.PENDING_PAYMENT)
                .total(total)
                .user(user)
                .build();
        orderBook.updateSaleList(sales);


        for (Sale sale : sales) {
            sale.updateOrderBook(orderBook);
        }

        orderBookRepository.save(orderBook);
        return orderBook;
    }


    @Transactional
    public OrderResponsDto updateOrderStatus(Long orderId, User user, OrderStatusRequestDto statusUpdate) {
        OrderBook orderBook = getOrderBook(orderId);

        SaleState status = statusUpdate.getStatus();
        if (orderBook.getStatus().equals(status)) {
            throw new IllegalArgumentException("변경 전과 후가 동일한 상태입니다.");
        }

        orderBook.updateStatus(status);
        List<SaleResponseDto> saleResponseDtos = new ArrayList<>();
        for (Sale sale : orderBook.getSaleList()) {
            sale.updateStatus(status);
            saleResponseDtos.add(SaleResponseDto.builder()
                    .status(sale.getStatus().toString())
                    .bookName(sale.getBook().getBookName())
                    .price(sale.getBook().getPrice())
                    .quantity(sale.getQuantity())
                    .total(sale.getQuantity() * sale.getBook().getPrice())
                    .createdAt(sale.getCreatedAt())
                    .build());
        }

        return OrderResponsDto.builder()
                .orderId(orderId)
                .status(status.toString())
                .total(orderBook.getTotal())
                .sales(saleResponseDtos)
                .build();

    }

    @Transactional
    public OrderResponsDto getOrderById(Long orderId, User user) {
        // 주문과 관련된 판매 항목을 포함하여 주문 내역을 조회합니다.
        OrderBook orderBook = orderBookRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new IllegalArgumentException("Order not found or you don't have access to this order."));

        List<SaleResponseDto> saleResponseDtos = new ArrayList<>();
        for (Sale sale : orderBook.getSaleList()) {
            saleResponseDtos.add(SaleResponseDto.builder()
                    .status(sale.getStatus().toString())
                    .bookName(sale.getBook().getBookName())
                    .price(sale.getBook().getPrice())
                    .quantity(sale.getQuantity())
                    .total(sale.getQuantity() * sale.getBook().getPrice())
                    .createdAt(sale.getCreatedAt())
                    .build());
        }

        return OrderResponsDto.builder()
                .orderId(orderBook.getId())
                .total(orderBook.getTotal())
                .total(orderBook.getTotal())
                .status(orderBook.getStatus().toString())
                .build();

    }
}
