package com.sparta.bookflex.domain.orderbook.dto;

import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.shipment.enums.ShipmentEnum;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderGetsResponseDto {
    private  Long orderId;
    private String orderNo;
    private String orderName;
    private int total;
    private LocalDateTime createdAt;
    private OrderState orderState;
    private List<OrderItemResponseDto> orderItemList;
//    private ShipmentEnum shipment;

    @Builder
    public OrderGetsResponseDto(Long orderId, String orderNo, String orderName, LocalDateTime createdAt, OrderState orderState, ShipmentEnum shipment,int total, List<OrderItemResponseDto> orderItemList) {
        this.orderItemList = orderItemList;
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.orderName = orderName;
        this.createdAt = createdAt;
        this.orderState = orderState;
  //      this.shipment = shipment;
        this.total = total;
    }

}
