package com.sparta.bookflex.domain.systemlog.enums;

import com.sparta.bookflex.common.utill.LogMessageFunction;
import com.sparta.bookflex.common.utill.LogMessageToken;
import lombok.Getter;

public enum ActionType implements LogMessageFunction<String, Object> {
    PAYMENT_COMPLETE(LogMessageToken.ORDER_ABOUT, LogMessageToken.PAYMENT, LogMessageToken.COMPLETE_ED),
    PAYMENT_CANCEL(LogMessageToken.ORDER_ABOUT, LogMessageToken.PAYMENT, LogMessageToken.CANCEL_ED),
    COUPON_GET(LogMessageToken.USER, LogMessageToken.COUPON_OBJ, LogMessageToken.ISSUE_ED),
    COUPON_USE(LogMessageToken.USER, LogMessageToken.COUPON_OBJ, LogMessageToken.USE_D),
    ORDERBOOK_COMPLETE(LogMessageToken.USER_S,LogMessageToken.ORDER,LogMessageToken.COMPLETE_ED),
    ORDERBOOK_CANCEL(LogMessageToken.USER_S, LogMessageToken.ORDER, LogMessageToken.CANCEL_ED),
    SHIPMENT_START(LogMessageToken.USER_S, LogMessageToken.SHIPMENT, LogMessageToken.START),
    SHIPMENT_CANCEL(LogMessageToken.USER_S, LogMessageToken.SHIPMENT, LogMessageToken.CANCEL_ED),
    REFUND_COMPLETE(LogMessageToken.ORDER_ABOUT, LogMessageToken.PAYMENT, LogMessageToken.REFUND_ED),
    REFUND_CANCEL(LogMessageToken.ORDER_ABOUT, LogMessageToken.REFUND, LogMessageToken.CANCEL_ED),
    LOGIN(LogMessageToken.USER, LogMessageToken.LOGIN),
    LOGOUT(LogMessageToken.USER, LogMessageToken.LOGOUT),
    BOOK_PURCHASE(LogMessageToken.BOOK_OBJ, LogMessageToken.PURCHASE);
//    POINT("");

    @Getter
    private String descriptionMsg;

    private String mainToken;
    private String objToken;
    private String endToken;

    ActionType(String mainToken, String endToken) {
        this.mainToken = mainToken;
        this.endToken = endToken;
    }

    ActionType(String mainToken, String objToken, String endToken) {
        this.mainToken = mainToken;
        this.objToken = objToken;
        this.endToken = endToken;
    }

    @Override
    public void apply(String main, Object value) {
        String message;
        if (objToken != null) {
            if(value != null) {
                message = main + mainToken + value + objToken + endToken;
            }
            else {
                message = main + mainToken + objToken + endToken;
            }
        }
        else {
            message = main + mainToken + endToken;
        }
        this.descriptionMsg = message;
    }

//    private static class LogMessageType {
//        public static String targetName;
//        public static String objectName;
//        public static long targetValue;
//        public static String PAYMENT_OK = String.format("%s상품에 대한 %d원 결제가 완료되었습니다.", targetName, targetValue);
//        public static String PAYMENT_FAIL = String.format("%s상품에 대한 %d원 결제가 취소되었습니다.", targetName, targetValue);
//        public static String COUPON_GET = String.format("%s님이 %s쿠폰을 발급받았습니다.", targetName, objectName);
//        public static String COUPON_USE = String.format("%s님이 %s쿠폰을 사용했습니다.", targetName, objectName);
//        public static String ORDERBOOK_OK = String.format("%s님의 주문이 완료됐습니다.", targetName);
//        public static String ORDERBOOK_FAIL = String.format("%s님의 주문이 취소됐습니다.", targetName);
//        public static String SHIPMENT_START = String.format("%s님의 배송이 출발했습니다.", targetName);
//        public static String SHIPMENT_CANCEL = String.format("%s님의 배송이 취소됐습니다.", targetName);
//        public static String REFUND_COMPLETE = String.format("%s상품에 대한 %d원 결제가 환불됐습니다.", targetName, targetValue);
//        public static String REFUND_CANCEL = String.format("%s상품에 대한 환불신청이 취소됐습니다.", targetName);
//        public static String LOGIN = String.format("%s님이 로그인했습니다.", targetName);
//        public static String LOGOUT = String.format("%s님이 로그아웃했습니다.", targetName);
//        public static String BOOK_PURCHASE = String.format("%s책을 구매했습니다", targetName);
//
//        public static void updateMessage() {
//            PAYMENT_OK = String.format("%s상품에 대한 %d원 결제가 완료되었습니다.", targetName, targetValue);
//            PAYMENT_FAIL = String.format("%s상품에 대한 %d원 결제가 취소되었습니다.", targetName, targetValue);
//            COUPON_GET = String.format("%s님이 쿠폰을 발급받았습니다.", targetName);
//            COUPON_USE = String.format("%s님이 쿠폰을 사용했습니다.", targetName);
//            ORDERBOOK_OK = String.format("%s님의 주문이 완료됐습니다.", targetName);
//            ORDERBOOK_FAIL = String.format("%s님의 주문이 취소됐습니다.", targetName);
//            SHIPMENT_START = String.format("%s님의 배송이 출발했습니다.", targetName);
//            SHIPMENT_CANCEL = String.format("%s님의 배송이 취소됐습니다.", targetName);
//            REFUND_COMPLETE = String.format("%s상품에 대한 %d원 결제가 환불됐습니다.", targetName, targetValue);
//            REFUND_CANCEL = String.format("%s상품에 대한 환불신청이 취소됐습니다.", targetName);
//            LOGIN = String.format("%s님이 로그인했습니다.", targetName);
//            LOGOUT = String.format("%s님이 로그아웃했습니다.", targetName);
//            BOOK_PURCHASE = String.format("%s책을 구매했습니다", targetName);
//        }
//    }
}