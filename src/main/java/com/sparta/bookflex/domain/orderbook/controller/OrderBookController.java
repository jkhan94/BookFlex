package com.sparta.bookflex.domain.orderbook.controller;

import com.sparta.bookflex.common.dto.CommonDto;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.orderbook.dto.OrderRequestDto;
import com.sparta.bookflex.domain.orderbook.dto.OrderResponsDto;
import com.sparta.bookflex.domain.orderbook.dto.OrderStatusRequestDto;
import com.sparta.bookflex.domain.orderbook.service.OrderBookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderBookController {

    private OrderBookService orderBookService;

    public OrderBookController(OrderBookService orderBookService) {
        this.orderBookService = orderBookService;
    }

    @PostMapping("")
    public ResponseEntity<CommonDto<Void>> createOrder(@RequestBody OrderRequestDto orderRequestDto,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderBookService.createOrder(orderRequestDto, userDetails.getUser());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonDto<>(HttpStatus.CREATED.value(), "주문이 완료되었습니다.", null));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<CommonDto<OrderResponsDto>> updateOrderStatus(@PathVariable Long orderId,
                                                       @RequestBody OrderStatusRequestDto statusUpdate,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderResponsDto updatedOrder = orderBookService.updateOrderStatus(orderId, userDetails.getUser(), statusUpdate);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(),  "주문상태가 변경되었습니다.",updatedOrder));
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<CommonDto<OrderResponsDto>> getOrderById(@PathVariable Long orderId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderResponsDto orderResponseDto = orderBookService.getOrderById(orderId, userDetails.getUser());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "주문조회에 성공했습니다.", orderResponseDto));
    }



}
