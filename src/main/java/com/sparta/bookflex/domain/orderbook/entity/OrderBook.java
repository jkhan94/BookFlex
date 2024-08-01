package com.sparta.bookflex.domain.orderbook.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.orderbook.dto.OrderShipResDto;
import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Getter
@Table(name = "order_book")
@NoArgsConstructor
public class OrderBook extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_book_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderState status;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "discountPrice", precision = 10, scale = 2)
    private BigDecimal discountPrice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "orderBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList;

    @OneToMany(mappedBy = "orderBook")
    private List<Sale> saleList;

    @Column
    private String carrier;

    @Column
    private String trackingNumber;

    @Builder
    public OrderBook(OrderState status, BigDecimal total, User user, boolean isCoupon, BigDecimal discountPrice, String orderNo) {
        this.status = status;
        this.user = user;
        this.discountPrice = discountPrice;
        if(discountPrice !=null) {
            this.total = total.subtract(discountPrice);}
        this.carrier = "dev.track.dummy";

        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime zonedDateTime = now.atZone(ZoneOffset.UTC);
        this.trackingNumber = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'09:00:00'Z'"));
    }

    public static OrderShipResDto toOrderShipRes(OrderBook orderBook) {
        return new OrderShipResDto(orderBook.getOrderNo()
            , orderBook.getUser().getUsername()
            , orderBook.getTrackingNumber()
            , orderBook.getCarrier());
    }

//        this.orderNumber = orderNumber;
//        this.username = username;
//        this.shipStartedAt = shipStartedAt;
//        this.deliverStartedAt = deliverStartedAt;
//        this.status = status;
//        this.carrier = carrier;

    public void updateSaleList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public void updateStatus(OrderState status) {
        this.status = status;
    }

    public String getOrderNo() {
        if (id != null && id != 0L) {
            orderNo = "ORD_" + id;
        }
        return orderNo;
    }
}
