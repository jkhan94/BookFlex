package com.sparta.bookflex.domain.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bookflex.common.config.TossPaymentConfig;
import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.exception.ErrorCode;
import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.repository.BookRepository;
import com.sparta.bookflex.domain.coupon.service.CouponService;
import com.sparta.bookflex.domain.orderbook.dto.OrderStatusRequestDto;
import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.orderbook.entity.OrderItem;
import com.sparta.bookflex.domain.orderbook.service.OrderBookService;
import com.sparta.bookflex.domain.payment.dto.*;
import com.sparta.bookflex.domain.payment.entity.Payment;
import com.sparta.bookflex.domain.payment.enums.PayType;
import com.sparta.bookflex.domain.payment.enums.PaymentStatus;
import com.sparta.bookflex.domain.payment.repository.PaymentRepository;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.sale.repository.SaleRepository;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
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

    @Value("${payment.toss.success_url}")
    private String successUrl;

    @Value("${payment.toss.fail_url}")
    private String failUrl;




//    @Transactional
//    public String createPayment(TossPaymentRequestDto requestDto, User user) {
//        try {
//            URL url = null;
//            URLConnection connection = null;
//            StringBuilder responseBody = new StringBuilder();
//
//            try {
//                url = new URL(TossPaymentConfig.PAYMENT_URL);
//                connection = url.openConnection();
//                connection.addRequestProperty("Content-Type", "application/json");
//                connection.setDoOutput(true);
//                connection.setDoInput(true);
//
//                final var jsonBody = getJsonObject(requestDto);
//
//                BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
//                bos.write(jsonBody.toJSONString().getBytes(StandardCharsets.UTF_8));
//                bos.flush();
//                bos.close();
//
//                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
//                String line = null;
//                while ((line = br.readLine()) != null) {
//                    responseBody.append(line);
//                }
//                br.close();
//            } catch (Exception e) {
//                throw new BusinessException(ErrorCode.FAIL_REQUEST_TOSS);
//            }
//
//            String jsonResponse = responseBody.toString();
//            String payToken =  getPayToken(jsonResponse);
//            String checkoutPage = getCheckoutPage(jsonResponse);
//
//
//            OrderBook orderBook = orderBookService.getOrderBook(requestDto.getOrderId());
//            Payment payment = Payment.builder()
//                    .total(requestDto.getAmount())
//                    .user(user)
//                    .payType(PayType.TOSS)
//                    .payToken(payToken)
//                    .status(PaymentStatus.PAY_STANDBY)
//                    .orderBook(orderBook)
//                    .build();
//
//            paymentRepository.save(payment);
//            return checkoutPage;
//        }catch (Exception e) {
//            System.out.println(e.getMessage());
//            throw new BusinessException(ErrorCode.PAYMENT_CREATION_FAILED);
//        }
//    }
//
//    private JSONObject getJsonObject(TossPaymentRequestDto requestDto) {
//        JSONObject jsonBody = new JSONObject();
//        jsonBody.put("orderNo", requestDto.getOrderNo());
//        jsonBody.put("amount", requestDto.getAmount());
//        jsonBody.put("amountTaxFree", requestDto.getAmountTaxFree());
//        jsonBody.put("productDesc", requestDto.getProductDesc());
//        jsonBody.put("apiKey",tossSecretKey);
//        jsonBody.put("autoExecute", true);
//        jsonBody.put("resultCallback", successUrl);
//        jsonBody.put("retUrl", successUrl);
//        jsonBody.put("retCancelUrl", failUrl);
//        return jsonBody;
//    }

//    public TossPaymentStatusDto checkPaymentStatus(String paymentKey) {
//        try {
//            String url = TossPaymentConfig.PAYMENT_URL + paymentKey;  // 실제 결제 상태 조회 엔드포인트
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "Bearer " + tossSecretKey);
//
//            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
//            ResponseEntity<TossPaymentStatusDto> response = restTemplate.exchange(
//                    url,
//                    HttpMethod.GET,
//                    requestEntity,
//                    TossPaymentStatusDto.class
//            );
//
//            if (response.getStatusCode() == HttpStatus.OK) {
//                return response.getBody();
//            } else {
//                throw new RuntimeException("Failed to check payment status: " + response.getStatusCode());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Exception occurred while checking payment status", e);
//        }
//    }
//
//    public static String getCheckoutPage(String jsonString) {
//        String key = "\"checkoutPage\":\"";
//        int startIndex = jsonString.indexOf(key);
//
//        if (startIndex != -1) {
//            startIndex += key.length();
//            int endIndex = jsonString.indexOf("\"", startIndex);
//            if (endIndex != -1) {
//                return jsonString.substring(startIndex, endIndex);
//            }
//        }
//        return null;
//    }
//
//    public static String getPayToken(String jsonString) {
//        String key = "\"payToken\":\"";
//        int startIndex = jsonString.indexOf(key);
//
//        if (startIndex != -1) {
//            startIndex += key.length();
//            int endIndex = jsonString.indexOf("\"", startIndex);
//            if (endIndex != -1) {
//                return jsonString.substring(startIndex, endIndex);
//            }
//        }
//        return null;
//    }


    @Transactional
    public void processPayment(User user, SuccessPayReqDto successPayReqDto) throws MessagingException, UnsupportedEncodingException {
        OrderBook orderBook = orderBookService.getOrderByOrderNo(successPayReqDto.getOrderId());
        orderBook.updateStatus(OrderState.SALE_COMPLETED);
        Payment payment = paymentRepository.findByOrderBook(orderBook);
        payment.updateStatus(PaymentStatus.PAY_COMPLETE);
        payment.updatePayToken(successPayReqDto.getPaymentKey());
        payment.updateIsSuccessYN(true);
        paymentRepository.save(payment);
        List<OrderItem> orderItemList = orderBook.getOrderItemList();
        for(OrderItem orderItem : orderItemList){
            Book book = orderItem.getBook();
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
    }
}
