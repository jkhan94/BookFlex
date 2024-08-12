package com.sparta.bookflex.domain.orderbook.service;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.exception.ErrorCode;
import com.sparta.bookflex.common.utill.LoggingSingleton;
import com.sparta.bookflex.domain.auth.service.AuthService;
import com.sparta.bookflex.domain.basket.service.BasketService;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.service.BookService;
import com.sparta.bookflex.domain.coupon.entity.Coupon;
import com.sparta.bookflex.domain.coupon.entity.UserCoupon;
import com.sparta.bookflex.domain.coupon.enums.DiscountType;
import com.sparta.bookflex.domain.coupon.service.CouponService;
import com.sparta.bookflex.domain.orderbook.dto.*;
import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.orderbook.entity.OrderItem;
import com.sparta.bookflex.domain.orderbook.repository.OrderBookQRepositoryImpl;
import com.sparta.bookflex.domain.orderbook.repository.OrderBookRepository;
import com.sparta.bookflex.domain.orderbook.repository.OrderItemRepository;
import com.sparta.bookflex.domain.payment.entity.Payment;
import com.sparta.bookflex.domain.payment.enums.PayType;
import com.sparta.bookflex.domain.payment.enums.PaymentStatus;
import com.sparta.bookflex.domain.payment.repository.PaymentRepository;
import com.sparta.bookflex.domain.photoimage.service.PhotoImageService;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.sale.repository.SaleRepository;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.enums.RoleType;
import jakarta.mail.MessagingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.bookflex.domain.user.enums.RoleType.ADMIN;

@Service
@RequiredArgsConstructor
public class OrderBookService {

    private final OrderItemRepository orderItemRepository;
    @Getter
    private final OrderBookRepository orderBookRepository;
    private final AuthService authService;
    private final BookService bookService;

    private final PhotoImageService photoImageService;
    private final SaleRepository saleRepository;

    private final CouponService couponService;

    private final LoggingSingleton logger = LoggingSingleton.getInstance();

    private final PaymentRepository paymentRepository;

    private final BasketService basketService;
    private final OrderBookQRepositoryImpl orderBookQRepositoryImpl;


    private Book getBook(Long bookId) {
        return bookService.getBookByBookId(bookId);
    }

    public OrderBook OrderBookByOrderNo(String orderNo) {
        return orderBookRepository.findByOrderNo(orderNo);
    }

    public OrderBook getOrderBook(Long orderId) {
        return orderBookRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

    }

    @Transactional
    public OrderCreateResponseDto createOrder(OrderRequestDto orderRequestDto, User user) {
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItemList = new ArrayList<>();
        List<Long> bookIds = new ArrayList<>();
        for (OrderRequestDto.OrderItemDto item : orderRequestDto.getItems()) {
            Book book = getBook(item.getBookId());
            bookIds.add(book.getId());
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
//만든 orderitemList를 기반으로 orderbook을 만든다.
        OrderBook orderBook = OrderBook.builder()
                .status(OrderState.PENDING_PAYMENT)
                .total(total)
                .user(user)
                .build();
        orderBook.updateOrderItemList(orderItemList);

        for (OrderItem orderItem : orderItemList) {
            orderItem.updateOrderBook(orderBook);
        }

        orderBookRepository.save(orderBook);
        orderBook.generateOrderNo();

        basketService.clear(bookIds, user);

        return OrderCreateResponseDto.builder()
                .orderId(orderBook.getId())
                .orderNo(orderBook.getOrderNo())
                .build();
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
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        List<OrderItemResponseDto> orderItemResponseDtoList = convertOrderItemsToDtoList(orderBook);


        return OrderResponsDto.builder()
                .orderId(orderBook.getId())
                .orderNo(orderBook.getOrderNo())
                .total(orderBook.getTotal())
                .total(orderBook.getTotal())
                .status(orderBook.getStatus().toString())
                .orderItemResponseDtoList(orderItemResponseDtoList)
                .build();
    }

//    public OrderBookTotalResDto getAllOrder(int page, int size) {
//
//        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
//        Pageable pageable = PageRequest.of(page-1, size, sort);
//        Page<OrderShipResDto> pageResdto = orderBookRepository.findAllByPagable(pageable).map(
//            OrderBook::toOrderShipRes);
//        Long totalCount = orderBookRepository.findTotalCount();
//
//        return new OrderBookTotalResDto(totalCount, pageResdto);
//    }


    @Transactional
    public void setCouponToOrder(Long orderId, Long userCouponId) {
        OrderBook orderBook = orderBookRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        UserCoupon userCoupon = couponService.findUserCouponById(userCouponId);

        orderBookRepository.save(orderBook);

    }

    @Transactional
    public OrderPaymentResponseDto createPayment(OrderPaymentRequestDto orderPaymentRequestDto, User user) {
        OrderBook orderBook = getOrderBook(orderPaymentRequestDto.getOrderId());
        UserCoupon userCoupon = null;
        Coupon coupon = null;
        BigDecimal discount = null;
        BigDecimal discountPerItem = null;
        BigDecimal total = orderBook.getTotal();
        List<OrderItem> orderItemList = orderBook.getOrderItemList();
        if (orderPaymentRequestDto.getUserCouponId() != null) {
            userCoupon = couponService.findUserCouponById(orderPaymentRequestDto.getUserCouponId());
            orderBook.updateUserCoupon(userCoupon);
            coupon = userCoupon.getCoupon();
            if (coupon.getDiscountType().equals(DiscountType.FIXED_AMOUNT)) {
                discount = coupon.getDiscountPrice();
            } else {
                discount = orderBook.getTotal().multiply(coupon.getDiscountPrice()).divide(BigDecimal.valueOf(100));
            }
            orderBook.updateDiscount(discount);
            int itemCount = orderItemList.size();
            total = orderBook.getDiscountTotal();
            discountPerItem = discount.divide(BigDecimal.valueOf(itemCount), BigDecimal.ROUND_HALF_EVEN);
        }

        List<Sale> saleList = new ArrayList<>();
        for (OrderItem orderItem : orderItemList) {
            Sale sale = Sale.builder()
                    .book(orderItem.getBook())
                    .price(orderItem.getPrice())
                    .quantity(orderItem.getQuantity())
                    .total(orderItem.getTotal())
                    .orderBook(orderBook)
                    .user(user)
                    .status(OrderState.PENDING_PAYMENT)
                    .build();
            if (discount != null) {
                sale.updateDiscount(discountPerItem);
            }
            saleRepository.save(sale);
            saleList.add(sale);
        }
        orderBook.updateSaleList(saleList);

        orderBookRepository.save(orderBook);

        Payment payment = Payment.builder()
                .user(user)
                .orderBook(orderBook)
                .payType(PayType.TOSS)
                .status(PaymentStatus.PAY_STANDBY)
                .discount(discount)
                .total(orderBook.getTotal())
                .build();
        paymentRepository.save(payment);


        String orderName = orderItemList.get(0).getBook().getBookName() + " 외 " + (orderItemList.size() - 1) + "개";


        return OrderPaymentResponseDto.builder()
                .orderId(orderBook.getId())
                .orderNo(orderBook.getOrderNo())
                .orderName(orderName)
                .customerEmail(user.getEmail())
                .customerName(user.getName())
                .customerMobilePhone(user.getPhoneNumber().replaceAll("-", ""))
                .paymentAmount(total.intValue())
                .build();
    }

    public OrderBook getOrderByOrderNo(String orderNo) {
        return orderBookRepository.findByOrderNo(orderNo);
    }

    @Transactional
    public Page<OrderGetsResponseDto> getOrders(User user, Pageable pageable, OrderState status, LocalDate startDate, LocalDate endDate, String username) {

        if (user.getAuth().equals(ADMIN)) {
            Page<OrderBook> orderBookPage = orderBookQRepositoryImpl.getOrderBooksByStatus(pageable, status, startDate, endDate, username);

            Page<OrderGetsResponseDto> orderGetsResponseDtoPage =
                    orderBookPage.map(orderBook -> OrderGetsResponseDto.builder()
                            .orderId(orderBook.getId())
                            .orderNo(orderBook.getOrderNo())
                            .orderName(orderBook.getOrderItemList().get(0).getBook().getBookName() + " 외 " + (orderBook.getOrderItemList().size() - 1) + "개")
                            .username(orderBook.getUser().getUsername())
                            .total(orderBook.getDiscountTotal().intValue())
                            .orderState(orderBook.getStatus())
                            .createdAt(orderBook.getCreatedAt())
                            .orderItemList(convertOrderItemsToDtoList(orderBook))
                            .build());

            return orderGetsResponseDtoPage;
        } else {
            Page<OrderBook> orderBookList = orderBookRepository.findByUser(user, pageable);
            return orderBookList.map(orderBook -> OrderGetsResponseDto.builder()
                    .orderId(orderBook.getId())
                    .orderNo(orderBook.getOrderNo())
                    .orderName(orderBook.getOrderItemList().get(0).getBook().getBookName() + " 외 " + (orderBook.getOrderItemList().size() - 1) + "개")
                    .username(orderBook.getUser().getUsername())
                    .total(orderBook.getDiscountTotal().intValue())
                    .orderState(orderBook.getStatus())
                    .createdAt(orderBook.getCreatedAt())
                    .orderItemList(convertOrderItemsToDtoList(orderBook))
                    .build());
        }


    }

    public List<OrderItemResponseDto> convertOrderItemsToDtoList(OrderBook orderBook) {
        return orderBook.getOrderItemList().stream()
                .map(this::convertToDto) // convertToDto 메서드 사용
                .collect(Collectors.toList());
    }

    public OrderItemResponseDto convertToDto(OrderItem orderItem) {
        Book book = orderItem.getBook();
        String bookName = book != null ? book.getBookName() : "";
        String photoImagePath = photoImageService.getPhotoImageUrl(book.getPhotoImage().getFilePath());

        return OrderItemResponseDto.builder()
                .orderItemId(orderItem.getId())
                .bookName(bookName)
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .total(orderItem.getTotal())
                .createdAt(orderItem.getCreatedAt())
                .photoImagePath(photoImagePath)
                .isReviewed(orderItem.getIsReviewed())
                .build();
    }
}
