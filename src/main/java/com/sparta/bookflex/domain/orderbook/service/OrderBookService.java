package com.sparta.bookflex.domain.orderbook.service;

import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.service.BookService;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.orderbook.repository.OrderBookRepository;
import com.sparta.bookflex.domain.sale.Enum.SaleState;
import com.sparta.bookflex.domain.sale.dto.OrderRequestDto;
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


}
