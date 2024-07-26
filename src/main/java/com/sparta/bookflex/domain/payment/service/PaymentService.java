package com.sparta.bookflex.domain.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bookflex.common.config.TossPaymentConfig;
import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.exception.ErrorCode;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.orderbook.service.OrderBookService;
import com.sparta.bookflex.domain.payment.dto.*;
import com.sparta.bookflex.domain.payment.entity.Payment;
import com.sparta.bookflex.domain.payment.enums.PayType;
import com.sparta.bookflex.domain.payment.enums.PaymentStatus;
import com.sparta.bookflex.domain.payment.repository.PaymentRepository;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.service.AuthService;
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
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Service
public class PaymentService {

    @Value("${payment.toss.test_secret_api_key}")
    private String tossSecretKey;
    private final RestTemplate restTemplate;
    private final AuthService authService;
    private final PaymentRepository paymentRepository;
    private final OrderBookService orderBookService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PaymentService(AuthService authService, PaymentRepository paymentRepository,
                          OrderBookService orderBookService,
                          RestTemplate restTemplate, ObjectMapper objectMapper,
                          ObjectMapper objectMapper1) {
        this.restTemplate = restTemplate;
        this.authService = authService;
        this.paymentRepository = paymentRepository;
        this.orderBookService = orderBookService;
        this.objectMapper = objectMapper1;
    }

    @Transactional
    public String createPayment(TossPaymentRequestDto requestDto, User user) {
        try {
            URL url = null;
            URLConnection connection = null;
            StringBuilder responseBody = new StringBuilder();

            try {
                url = new URL("https://pay.toss.im/api/v2/payments");
                connection = url.openConnection();
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                final var jsonBody = getJsonObject(requestDto);

                BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
                bos.write(jsonBody.toJSONString().getBytes(StandardCharsets.UTF_8));
                bos.flush();
                bos.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                String line = null;
                while ((line = br.readLine()) != null) {
                    responseBody.append(line);
                }
                br.close();
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.FAIL_REQUEST_TOSS);
            }

            String jsonResponse = responseBody.toString();
            String payToken =  getPayToken(jsonResponse);
            String checkoutPage = getCheckoutPage(jsonResponse);


            OrderBook orderBook = orderBookService.getOrderBook((long)Integer.parseInt(requestDto.getOrderNo()));
            Payment payment = Payment.builder()
                    .total(requestDto.getAmount())
                    .user(user)
                    .payType(PayType.TOSS)
                    .payToken(payToken)
                    .status(PaymentStatus.PAY_STANDBY)
                    .orderBook(orderBook)
                    .build();

            paymentRepository.save(payment);
            return checkoutPage;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BusinessException(ErrorCode.PAYMENT_CREATION_FAILED);
        }
    }

    private JSONObject getJsonObject(TossPaymentRequestDto requestDto) {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("orderNo", requestDto.getOrderNo());
        jsonBody.put("amount", requestDto.getAmount());
        jsonBody.put("amountTaxFree", requestDto.getAmountTaxFree());
        jsonBody.put("productDesc", requestDto.getProductDesc());
        jsonBody.put("apiKey",tossSecretKey);
        jsonBody.put("autoExecute", true);
        jsonBody.put("resultCallback", requestDto.getResultCallback());
        jsonBody.put("retUrl", requestDto.getRetUrl());
        jsonBody.put("retCancelUrl", requestDto.getRetCancelUrl());
        return jsonBody;
    }

    public TossPaymentStatusDto checkPaymentStatus(String paymentKey) {
        try {
            String url = TossPaymentConfig.PAYMENT_URL + paymentKey;  // 실제 결제 상태 조회 엔드포인트
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tossSecretKey);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<TossPaymentStatusDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    TossPaymentStatusDto.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to check payment status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception occurred while checking payment status", e);
        }
    }

    public static String getCheckoutPage(String jsonString) {
        String key = "\"checkoutPage\":\"";
        int startIndex = jsonString.indexOf(key);

        if (startIndex != -1) {
            startIndex += key.length();
            int endIndex = jsonString.indexOf("\"", startIndex);
            if (endIndex != -1) {
                return jsonString.substring(startIndex, endIndex);
            }
        }
        return null;
    }

    public static String getPayToken(String jsonString) {
        String key = "\"payToken\":\"";
        int startIndex = jsonString.indexOf(key);

        if (startIndex != -1) {
            startIndex += key.length();
            int endIndex = jsonString.indexOf("\"", startIndex);
            if (endIndex != -1) {
                return jsonString.substring(startIndex, endIndex);
            }
        }
        return null;
    }


//    public TossRefundResponseDto processRefund(String paymentKey, String amount, String reason) {
//        try {
//            String url = "https://api.tosspayments.com/v1/refunds";
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
//            headers.set("Authorization", "Bearer " + tossSecretKey);
//
//            TossRefundRequestDto requestDto = TossRefundRequestDto.builder()
//                    .paymentKey(paymentKey)
//                    .amount(amount)
//                    .reason(reason)
//                    .build();
//
//            HttpEntity<TossRefundRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);
//            ResponseEntity<TossRefundResponseDto> response = restTemplate.exchange(
//                    url,
//                    HttpMethod.POST,
//                    requestEntity,
//                    TossRefundResponseDto.class
//            );
//
//            if (response.getStatusCode() == HttpStatus.OK) {
//                return response.getBody();
//            } else {
//                throw new RuntimeException("Failed to process refund: " + response.getStatusCode());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Exception occurred while processing refund", e);
//        }
//    }
}
