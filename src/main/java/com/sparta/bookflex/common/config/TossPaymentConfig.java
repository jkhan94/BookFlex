package com.sparta.bookflex.common.config;


import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Getter
public class TossPaymentConfig {


    @Value("${payment.toss.test_client_api_key}")
    private String testClientApiKey;


    @Value("${payment.toss.test_secret_api_key}")
    private String testSecretKey;


    private String successUrl="http://localhost:8080/payments/success";


    private String failUrl= "http://localhost:8080/payments/fail";

    //토스페이먼츠에 결제 승인 요처 보낼 URL
    public static final String PAYMENT_URL = "https://pay.toss.im/api/v2/payments";

    public static final String REFUND_URL = "https://pay.toss.im/api/v2/refunds";

    public static final String STATE_URL = "https://pay.toss.im/api/v2/status";

    public static final String EXECUTE_URL = "https://pay.toss.im/api/v2/execute";

}
