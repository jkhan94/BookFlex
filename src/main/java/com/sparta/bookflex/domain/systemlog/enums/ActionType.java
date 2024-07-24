package com.sparta.bookflex.domain.systemlog.enums;

import lombok.Getter;

public enum ActionType {
    PAYMENT_COMPLETE(LogMessageType.PAYMENT_OK),
    PAYMENT_CANCEL(LogMessageType.PAYMENT_FAIL),
    COUPON_GET(LogMessageType.COUPON_GET),
    COUPON_USE(LogMessageType.COUPON_USE),
    ORDERBOOK_COMPLETE(LogMessageType.ORDERBOOK_OK),
    ORDERBOOK_CANCEL(LogMessageType.ORDERBOOK_FAIL),
    SHIPMENT_START(LogMessageType.SHIPMENT_START),
    SHIPMENT_CANCEL(LogMessageType.SHIPMENT_CANCEL),
    REFUND_COMPLETE(LogMessageType.REFUND_COMPLETE),
    REFUND_CANCEL(LogMessageType.REFUND_CANCEL),
    LOGIN(LogMessageType.LOGIN),
    LOGOUT(LogMessageType.LOGOUT),
    POINT("");

    @Getter
    private final String descriptionMsg;

    private ActionType(String msg) {
        this.descriptionMsg = msg;
    }

    public void setTarget(String name, long value) {
        LogMessageType.targetName = name;
        LogMessageType.targetValue = value;
        LogMessageType.updateMessage();
    }

    private static class LogMessageType {
        public static String targetName;
        public static long targetValue;
        public static String PAYMENT_OK = String.format("%s상품에 대한 %d원 결제가 완료되었습니다.", targetName, targetValue);
        public static String PAYMENT_FAIL = String.format("%s상품에 대한 %d원 결제가 취소되었습니다.", targetName, targetValue);
        public static String COUPON_GET = String.format("%s님이 쿠폰을 발급받았습니다.", targetName);
        public static String COUPON_USE = String.format("%s님이 쿠폰을 사용했습니다.", targetName);
        public static String ORDERBOOK_OK = String.format("%s님의 주문이 완료됐습니다.", targetName);
        public static String ORDERBOOK_FAIL = String.format("%s님의 주문이 취소됐습니다.", targetName);
        public static String SHIPMENT_START = String.format("%s님의 배송이 출발했습니다.", targetName);
        public static String SHIPMENT_CANCEL = String.format("%s님의 배송이 취소됐습니다.", targetName);
        public static String REFUND_COMPLETE = String.format("%s상품에 대한 %d원 결제가 환불됐습니다.", targetName, targetValue);
        public static String REFUND_CANCEL = String.format("%s상품에 대한 환불신청이 취소됐습니다.", targetName);
        public static String LOGIN = String.format("%s님이 로그인했습니다.", targetName);
        public static String LOGOUT = String.format("%s님이 로그아웃했습니다.", targetName);

        public static void updateMessage() {
            PAYMENT_OK = String.format("%s상품에 대한 %d원 결제가 완료되었습니다.", targetName, targetValue);
            PAYMENT_FAIL = String.format("%s상품에 대한 %d원 결제가 취소되었습니다.", targetName, targetValue);
            COUPON_GET = String.format("%s님이 쿠폰을 발급받았습니다.", targetName);
            COUPON_USE = String.format("%s님이 쿠폰을 사용했습니다.", targetName);
            ORDERBOOK_OK = String.format("%s님의 주문이 완료됐습니다.", targetName);
            ORDERBOOK_FAIL = String.format("%s님의 주문이 취소됐습니다.", targetName);
            SHIPMENT_START = String.format("%s님의 배송이 출발했습니다.", targetName);
            SHIPMENT_CANCEL = String.format("%s님의 배송이 취소됐습니다.", targetName);
            REFUND_COMPLETE = String.format("%s상품에 대한 %d원 결제가 환불됐습니다.", targetName, targetValue);
            REFUND_CANCEL = String.format("%s상품에 대한 환불신청이 취소됐습니다.", targetName);
            LOGIN = String.format("%s님이 로그인했습니다.", targetName);
            LOGOUT = String.format("%s님이 로그아웃했습니다.", targetName);
        }
    }
}