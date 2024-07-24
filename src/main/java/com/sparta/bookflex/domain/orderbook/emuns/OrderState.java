package com.sparta.bookflex.domain.orderbook.emuns;

public enum OrderState {
    PENDING_PAYMENT("결제대기"),
    ITEM_PREPARING("상품준비"),
    IN_DELIVERY("배송중"),
    DELIVERY_COMPLETED("배송완료"),
    SALE_COMPLETED("판매완료"),
    ORDER_CANCELLED("주문취소");

    private final String desscription;

    OrderState(String desscription){
        this.desscription = desscription;
    }

    public String getDesscription(){
        return this.desscription;
    }
}
