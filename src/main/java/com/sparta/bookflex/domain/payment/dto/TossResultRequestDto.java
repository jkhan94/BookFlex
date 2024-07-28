package com.sparta.bookflex.domain.payment.dto;

import lombok.Getter;

@Getter
public class TossResultRequestDto {
    private String status;
    private String payToken;
    private String orderNo;
    private String payMethod;
    private int amount;
    private int discountedAmount;
    private int paidAmount;
    private String paidTs;
    private String transactionId;
    private int cardCompanyCode;
    private String cardAuthorizationNo;
    private int spreadOut;
    private boolean noInterest;
    private String cardMethodType;
    private String cardUserType;
    private String cardNumber;
    private String cardBinNumber;
    private String cardNum4Print;
    private String salesCheckLinkUrl;
}
