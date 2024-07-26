package com.sparta.bookflex.domain.sale.Enum;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SaleState {
    PENDING_PAYMENT("결제대기"),
    ITEM_PREPARING("상품준비"),
    IN_DELIVERY("배송중"),
    DELIVERY_COMPLETED("배송완료"),
    SALE_COMPLETED("판매완료"),
    ORDER_CANCELLED("주문취소");

    @JsonValue
    private final String desscription;

    SaleState(String desscription){
        this.desscription = desscription;
    }

    public String getDesscription(){
        return this.desscription;
    }

}
