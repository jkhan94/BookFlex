package com.sparta.bookflex.domain.orderbook.controller;

import com.sparta.bookflex.common.dto.CommonDto;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.book.entity.BookStatus;
import com.sparta.bookflex.domain.orderbook.dto.*;
import com.sparta.bookflex.domain.orderbook.dto.OrderBookTotalResDto;
import com.sparta.bookflex.domain.orderbook.dto.OrderRequestDto;
import com.sparta.bookflex.domain.orderbook.dto.OrderResponsDto;
import com.sparta.bookflex.domain.orderbook.dto.OrderStatusRequestDto;
import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.orderbook.service.OrderBookService;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderBookController {

    private OrderBookService orderBookService;

    public OrderBookController(OrderBookService orderBookService) {
        this.orderBookService = orderBookService;
    }

    @PostMapping("")
    public ResponseEntity<OrderCreateResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderCreateResponseDto responseDto = orderBookService.createOrder(orderRequestDto, userDetails.getUser());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<CommonDto<OrderResponsDto>> updateOrderStatus(@PathVariable Long orderId,
                                                                        @RequestBody OrderStatusRequestDto statusUpdate,
                                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) throws MessagingException, UnsupportedEncodingException {
        OrderResponsDto updatedOrder = orderBookService.updateOrderStatus(orderId, userDetails.getUser(), statusUpdate);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "주문상태가 변경되었습니다.", updatedOrder));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponsDto> getOrderById(@PathVariable Long orderId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderResponsDto orderResponseDto = orderBookService.getOrderById(orderId, userDetails.getUser());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderResponseDto);
    }

    @PostMapping("/payment")
    public ResponseEntity<?> updateUserCoupon(@RequestBody OrderPaymentRequestDto OrderPaymentRequestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderPaymentResponseDto responseDto = orderBookService.createPayment(OrderPaymentRequestDto, userDetails.getUser());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }


    @GetMapping("")
    public ResponseEntity<Page<OrderGetsResponseDto>> getOrders(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                @RequestParam(name = "status", required = false, defaultValue = "") OrderState status,
                                                                @RequestParam(name = "startDate", required = false, defaultValue = "19000101") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate startDate,
                                                                @RequestParam(name = "endDate", required = false, defaultValue = "99991231") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate endDate,
                                                                @RequestParam(name = "username", required = false) String username)
    {
        Page<OrderGetsResponseDto> orderGetsResponseDtos = orderBookService.getOrders(userDetails.getUser(), pageable, status, startDate, endDate, username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderGetsResponseDtos);
    }


}
