package com.sparta.bookflex.domain.orderbook.service;

import com.sparta.bookflex.common.utill.LoggingSingleton;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.service.BookService;
import com.sparta.bookflex.domain.orderbook.dto.OrderItemResponseDto;
import com.sparta.bookflex.domain.orderbook.dto.OrderRequestDto;
import com.sparta.bookflex.domain.orderbook.dto.OrderResponsDto;
import com.sparta.bookflex.domain.orderbook.dto.OrderStatusRequestDto;
import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.orderbook.entity.OrderItem;
import com.sparta.bookflex.domain.orderbook.repository.OrderBookRepository;
import com.sparta.bookflex.domain.orderbook.repository.OrderItemRepository;
import com.sparta.bookflex.domain.photoimage.service.PhotoImageService;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.sale.repository.SaleRepository;
import com.sparta.bookflex.domain.systemlog.enums.ActionType;
import com.sparta.bookflex.domain.systemlog.repository.TraceOfUserLogRepository;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.service.AuthService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderBookService {

    private OrderItemRepository orderItemRepository;
    private OrderBookRepository orderBookRepository;
    private final AuthService authService;
    private final BookService bookService;
    private final TraceOfUserLogRepository traceOfUserLogRepository;

    private final PhotoImageService photoImageService;
    private final SaleRepository saleRepository;

    @Autowired
    public OrderBookService(OrderItemRepository orderItemRepository,
                            OrderBookRepository orderBookRepository,
                            AuthService authService, BookService bookService,
                            TraceOfUserLogRepository traceOfUserLogRepository,
                            PhotoImageService photoImageService,
                            SaleRepository saleRepository) {


        this.authService = authService;
        this.bookService = bookService;
        this.orderBookRepository = orderBookRepository;
        this.traceOfUserLogRepository = traceOfUserLogRepository;
        this.orderItemRepository = orderItemRepository;
        this.photoImageService = photoImageService;
        this.saleRepository = saleRepository;
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
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderRequestDto.OrderItemDto item : orderRequestDto.getItems()) {
            Book book = getBook(item.getBookId());
            book.decreaseStock(item.getQuantity());
            BigDecimal price = item.getPrice();
            BigDecimal itemTotal = BigDecimal.valueOf(item.getQuantity()).multiply(price);
            total = total.add(itemTotal);

            OrderItem orderItem = OrderItem.builder()
                .quantity(item.getQuantity())
                .book(book)
                .price(price)
                .orderBook(null)
                .build();
            orderItemList.add(orderItem);
        }

        OrderBook orderBook = OrderBook.builder()
            .status(OrderState.PENDING_PAYMENT)
            .total(total)
            .user(user)
            .build();
        orderBook.updateSaleList(orderItemList);

        for (OrderItem orderItem : orderItemList) {
            orderItem.updateOrderBook(orderBook);
        }

        for (OrderItem orderItem : orderItemList) {
            String bookName = orderItem.getBook().getBookName();
            traceOfUserLogRepository.save(
                LoggingSingleton.userLogging(ActionType.BOOK_PURCHASE, user, bookName, 0, orderItem.getBook()));
        }

        List<Sale> saleList = new ArrayList<>();

        for(OrderItem orderItem : orderItemList) {
            Sale sale = new Sale(orderItem, OrderState.PENDING_PAYMENT);
            saleList.add(sale);
            saleRepository.save(sale);
        }

        orderBookRepository.save(orderBook);
        return orderBook;
    }


    @Transactional
    public OrderResponsDto updateOrderStatus(Long orderId, User user, OrderStatusRequestDto statusUpdate) throws MessagingException, UnsupportedEncodingException {
        OrderBook orderBook = getOrderBook(orderId);

        OrderState status = statusUpdate.getStatus();
        if (orderBook.getStatus().equals(status)) {
            throw new IllegalArgumentException("변경 전과 후가 동일한 상태입니다.");
        }

        orderBook.updateStatus(status);
        boolean isOrderCancelled = status.equals(OrderState.ORDER_CANCELLED) || status.equals(OrderState.SALE_COMPLETED) || status.equals(OrderState.REFUND_PROCESSING);

//        EmailMessage emailMessage = EmailMessage.builder()
//                .to(user.getEmail())
//                .subject("[bookFlex] 배송현황안내")
//                .message("배송 현황을 아래와 같이 안내드립니다.").build();
//
//        emailService.sendEmail(emailMessage, orderBook);
        if (isOrderCancelled) {
            for (Sale sale : orderBook.getSaleList()) {
                sale.updateStatus(statusUpdate.getStatus());
            }
        }

        List<OrderItemResponseDto> orderItemResponseDtoList = new ArrayList<>();
        for (OrderItem orderItem : orderBook.getOrderItemList()) {
            Book book = orderItem.getBook();
            if (isOrderCancelled) {
                book.increaseStock(orderItem.getQuantity());
            }
            OrderItemResponseDto orderItemResponseDto = OrderItemResponseDto.builder()
                    .orderItemId(orderItem.getOrderBook().getId())
                    .price(orderItem.getPrice())
                    .total(orderItem.getPrice())
                    .createdAt(orderItem.getCreatedAt())
                    .bookName(orderItem.getBook().getBookName())
                    .quantity(orderItem.getQuantity())
                    .photoImagePath(photoImageService.getPhotoImageUrl(orderItem.getBook().getPhotoImage().getFilePath()))
                    .build();

            orderItemResponseDtoList.add(orderItemResponseDto);
        }

        return OrderResponsDto.builder()
                .orderId(orderId)
                .status(status.toString())
                .total(orderBook.getTotal())
                .orderItemResponseDtoList(orderItemResponseDtoList)
                .build();

    }

    @Transactional
    public OrderResponsDto getOrderById(Long orderId, User user) {
        // 주문과 관련된 판매 항목을 포함하여 주문 내역을 조회합니다.
        OrderBook orderBook = orderBookRepository.findByIdAndUser(orderId, user)
            .orElseThrow(() -> new IllegalArgumentException("Order not found or you don't have access to this order."));

        List<OrderItemResponseDto> orderItemResponseDtoList = new ArrayList<>();
        for (OrderItem orderItem : orderBook.getOrderItemList()) {
            OrderItemResponseDto orderItemResponseDto = OrderItemResponseDto.builder()
                .orderItemId(orderItem.getOrderBook().getId())
                .price(orderItem.getPrice())
                .total(orderItem.getPrice())
                .createdAt(orderItem.getCreatedAt())
                .bookName(orderItem.getBook().getBookName())
                .quantity(orderItem.getQuantity())
                .build();

            orderItemResponseDtoList.add(orderItemResponseDto);
        }

        return OrderResponsDto.builder()
            .orderId(orderBook.getId())
            .total(orderBook.getTotal())
            .total(orderBook.getTotal())
            .status(orderBook.getStatus().toString())
            .orderItemResponseDtoList(orderItemResponseDtoList)
            .build();

    }
}
