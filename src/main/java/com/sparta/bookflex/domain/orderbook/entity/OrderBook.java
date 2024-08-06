package com.sparta.bookflex.domain.orderbook.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.coupon.entity.Coupon;
import com.sparta.bookflex.domain.coupon.entity.UserCoupon;
import com.sparta.bookflex.domain.orderbook.dto.OrderShipResDto;
import com.sparta.bookflex.domain.orderbook.emuns.OrderState;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.shipment.entity.Shipment;
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
@NoArgsConstructor
public class OrderBook extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_book_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private OrderState status;

    @Column(name = "total",precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "discount",precision = 10, scale = 2)
    private BigDecimal discount;

    @Column(name = "discount_total",precision = 10, scale = 2)
    private BigDecimal discountTotal;

    @Column(name = "is_coupon")
    private boolean isCoupon;

    @Column(name = "discountPrice", precision = 10, scale = 2)
    private BigDecimal discountPrice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToOne
    @JoinColumn(name = "userCoupon_id")
    UserCoupon userCoupon;

    @OneToOne
    @JoinColumn(name = "shipment_id")
    Shipment shipment;

    @OneToMany(mappedBy = "orderBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList;

    @OneToMany(mappedBy = "orderBook")
    private List<Sale> saleList;

    @Transient
    private final String PREFIX = "BookFlexA";

    @Column
    private String carrier;

    @Column
    private String trackingNumber;

    @Builder
    public OrderBook(OrderState status, BigDecimal total, User user, BigDecimal discountPrice,String orderNo) {
        this.status = status;
        this.user = user;
        this.discount = discountPrice != null ? discountPrice : BigDecimal.ZERO;
        this.total = total ;
        this.orderNo = orderNo;
        this.discountTotal = total.subtract(discountPrice != null ? discountPrice : BigDecimal.ZERO);
        this.isCoupon = false;
        this.carrier = "dev.track.dummy";

        LocalDateTime now = LocalDateTime.now().minusDays(1);
        ZonedDateTime zonedDateTime = now.atZone(ZoneOffset.UTC);
        this.trackingNumber = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'09:00:00'Z'"));
    }

    public static OrderShipResDto toOrderShipRes(OrderBook orderBook) {
        return new OrderShipResDto(orderBook.generateOrderNo()
            , orderBook.getUser().getUsername()
            , orderBook.getTrackingNumber()
            , orderBook.getCarrier());
    }

    public void updateOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public void updateSaleList(List<Sale> saleList) {
        this.saleList = saleList;
    }

    public void updateStatus(OrderState status) {
        this.status = status;
    }

    public void updateDiscount(BigDecimal discount) {
        this.discount = discount;
        this.discountTotal = this.total.subtract(discount);
        this.isCoupon = true;
    }

    public String generateOrderNo() {
        if (this.id != null) {
            this.orderNo = String.format("%s-%d", PREFIX, this.id);
        }
        return orderNo;
    }

    public void updateShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public void updateUserCoupon(UserCoupon userCoupon) {
        this.userCoupon = userCoupon;
    }
}
