package com.sparta.bookflex.domain.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.exception.ErrorCode;
import com.sparta.bookflex.common.utill.LoggingSingleton;
import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.auth.service.AuthService;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.repository.BookRepository;
import com.sparta.bookflex.domain.coupon.service.CouponService;
import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.orderbook.entity.OrderItem;
import com.sparta.bookflex.domain.orderbook.service.OrderBookService;
import com.sparta.bookflex.domain.payment.dto.FailPayReqDto;
import com.sparta.bookflex.domain.payment.dto.SuccessPayReqDto;
import com.sparta.bookflex.domain.payment.entity.Payment;
import com.sparta.bookflex.domain.payment.enums.PaymentStatus;
import com.sparta.bookflex.domain.payment.repository.PaymentRepository;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.sale.repository.SaleRepository;
import com.sparta.bookflex.domain.shipment.service.ShipmentService;
import com.sparta.bookflex.domain.systemlog.enums.ActionType;
import com.sparta.bookflex.domain.systemlog.repository.SystemLogRepository;
import com.sparta.bookflex.domain.systemlog.repository.TraceOfUserLogRepository;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private String tossSecretKey="sk_test_w5lNQylNqa5lNQe013Nq";
    private final RestTemplate restTemplate;
    private final AuthService authService;
    private final PaymentRepository paymentRepository;
    private final OrderBookService orderBookService;
    private final ObjectMapper objectMapper;
    private final CouponService couponService;
    private final BookRepository bookRepository;
    private final SaleRepository saleRepository;
    private final TraceOfUserLogRepository traceOfUserLogRepository;
    private final SystemLogRepository systemLogRepository;
    private final ShipmentService shipmentService;


    @Value("${payment.toss.success_url}")
    private String successUrl;

    @Value("${payment.toss.fail_url}")
    private String failUrl;



    @Transactional
    public void processPayment(User user, SuccessPayReqDto successPayReqDto)  {
        OrderBook orderBook = orderBookService.getOrderByOrderNo(successPayReqDto.getOrderId());
        orderBook.updateStatus(OrderState.SALE_COMPLETED);
        Payment payment = paymentRepository.findByOrderBook(orderBook);
        payment.updateStatus(PaymentStatus.PAY_COMPLETE);
        payment.updatePayToken(successPayReqDto.getPaymentKey());
        payment.updateIsSuccessYN(true);
        paymentRepository.save(payment);
        List<OrderItem> orderItemList = orderBook.getOrderItemList();
        for(OrderItem orderItem : orderItemList){
            Book book = bookRepository.findByIdForUpdate(orderItem.getBook().getId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND));

            book.decreaseStock(orderItem.getQuantity());
            bookRepository.save(book);
        }
        if(orderBook.isCoupon()){
            orderBook.getUserCoupon().updateStatus();
        }

        List<Sale> saleList = orderBook.getSaleList();
        for(Sale sale : saleList){
            sale.updateStatus(OrderState.SALE_COMPLETED);
            saleRepository.save(sale);
        }

        for(OrderItem item : orderBook.getOrderItemList()) {
            String bookName = item.getBook().getBookName();
            traceOfUserLogRepository.save(
                LoggingSingleton.userLogging(ActionType.BOOK_PURCHASE, user, item.getBook()));
        }

        shipmentService.createShipment(orderBook, user);

        systemLogRepository.save(
            LoggingSingleton.Logging(ActionType.PAYMENT_COMPLETE, orderBook));

        //emailService.sendEmail(EmailMessage.builder()
        //        .to(user.getEmail())
        //        .subject("[bookFlex] 배송현황안내")
        //        .message("배송 현황을 아래와 같이 안내드립니다.").build(),
        //        orderBook);

    }

    public void processFailPayment(Timestamped user, FailPayReqDto failPayReqDto) {
        OrderBook orderBook = orderBookService.getOrderByOrderNo(failPayReqDto.getOrderId());
        Payment payment = paymentRepository.findByOrderBook(orderBook);
        payment.updateStatus(PaymentStatus.PAY_CANCEL);
        payment.updateIsSuccessYN(false);
        paymentRepository.save(payment);
        orderBook.updateStatus(OrderState.ORDER_CANCELLED);

        systemLogRepository.save(
            LoggingSingleton.Logging(ActionType.PAYMENT_CANCEL, orderBook));
    }
}
