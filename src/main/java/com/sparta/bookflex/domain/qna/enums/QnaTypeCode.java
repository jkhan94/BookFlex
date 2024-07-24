package com.sparta.bookflex.domain.qna.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum QnaTypeCode {
    PRODUCT(QnaCode.PRODUCT),
    ORDER_PAYMENT(QnaCode.ORDER_PAYMENT),
    DELIVERY(QnaCode.DELIVERY),
    RETURN_EXCHANGE(QnaCode.RETURN_EXCHANGE),
    IMPROVEMENT(QnaCode.IMPROVEMENT);

    final private String typeName;

    public String getQnaTypeName() {
        return typeName;
    }

    private QnaTypeCode(String typeName) {
        this.typeName = typeName;
    }

    @JsonCreator
    public static QnaTypeCode toJson(String qnaType) {
        for (QnaTypeCode type : QnaTypeCode.values()) {
            if (type.getTypeName().equals(qnaType)) {
                return type;
            }
        }
        return null;
    }

    @JsonValue
    public String getTypeName() {
        return typeName;
    }

    private static class QnaCode {
        public static final String PRODUCT = "상품";
        public static final String ORDER_PAYMENT = "주문/결제";
        public static final String DELIVERY = "배송";
        public static final String RETURN_EXCHANGE = "취소/교환/환불";
        public static final String IMPROVEMENT = "사이트 이용 오류/개선";
    }
}