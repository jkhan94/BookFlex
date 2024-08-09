package com.sparta.bookflex.domain.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bookflex.common.utill.LoggingSingleton;
import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.auth.service.AuthService;
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
import com.sparta.bookflex.domain.shipment.service.ShipmentService;
import com.sparta.bookflex.domain.systemlog.enums.ActionType;
import com.sparta.bookflex.domain.systemlog.repository.SystemLogRepository;
import com.sparta.bookflex.domain.systemlog.repository.TraceOfUserLogRepository;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class PaymentService {

    private String tossSecretKey="sk_test_w5lNQylNqa5lNQe013Nq";
    private final RestTemplate restTemplate;
    private final AuthService authService;
    private final PaymentRepository paymentRepository;
    private final OrderBookService orderBookService;
    private final ObjectMapper objectMapper;
    private final CouponService couponService;
    private final TraceOfUserLogRepository traceOfUserLogRepository;
    private final SystemLogRepository systemLogRepository;
    private final ShipmentService shipmentService;


    @Value("${payment.toss.success_url}")
    private String successUrl;

    @Value("${payment.toss.fail_url}")
    private String failUrl;



    @Autowired
    public PaymentService(AuthService authService, PaymentRepository paymentRepository,
                          OrderBookService orderBookService,
                          RestTemplate restTemplate, ObjectMapper objectMapper,
                             CouponService couponService,
                          TraceOfUserLogRepository traceOfUserLogRepository,
                          SystemLogRepository systemLogRepository,
                          ShipmentService shipmentService) {
        this.restTemplate = restTemplate;
        this.authService = authService;
        this.paymentRepository = paymentRepository;
        this.orderBookService = orderBookService;
        this.objectMapper = objectMapper;
        this.couponService = couponService;
        this.traceOfUserLogRepository = traceOfUserLogRepository;
        this.systemLogRepository = systemLogRepository;
        this.shipmentService = shipmentService;
    }

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

        if(orderBook.isCoupon()){
            orderBook.getUserCoupon().updateStatus();
        }

        List<Sale> saleList = orderBook.getSaleList();
        for(Sale sale : saleList){
            sale.updateStatus(OrderState.SALE_COMPLETED);
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
