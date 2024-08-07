package com.sparta.bookflex.common.utill;

import com.sparta.bookflex.domain.coupon.entity.UserCoupon;
import com.sparta.bookflex.domain.orderbook.entity.OrderBook;
import com.sparta.bookflex.domain.systemlog.entity.SystemLog;
import com.sparta.bookflex.domain.systemlog.entity.TraceOfUserLog;
import com.sparta.bookflex.domain.systemlog.enums.ActionType;
import com.sparta.bookflex.domain.user.entity.User;

import java.util.Optional;

public class LoggingSingleton {

    private LoggingSingleton() {
    }

    ;

    private static class SingletonGetter {
        private static final LoggingSingleton INSTANCE = new LoggingSingleton();
    }

    public static LoggingSingleton getInstance() {
        return SingletonGetter.INSTANCE;
    }

    // Logging 매개변수 일반화 작업
    public static SystemLog Logging(ActionType type, Object target) {
        String name = "";
        User user = null;
        Object value = null;
        switch (type) {
            case PAYMENT_COMPLETE, PAYMENT_CANCEL -> {
                OrderBook orderBook = (OrderBook) target;
                name = orderBook.getOrderNo();
                value = orderBook.getTotal();
                user = orderBook.getUser();
            }
            case COUPON_GET, COUPON_USE -> {
                UserCoupon userCoupon = (UserCoupon) target;
                name = userCoupon.getUser().getUsername();
                value = userCoupon.getCoupon().getCouponName();
                user = userCoupon.getUser();
            }
            case ORDERBOOK_COMPLETE, ORDERBOOK_CANCEL -> {
            }
            case SHIPMENT_START, SHIPMENT_CANCEL -> {
            }
            case REFUND_COMPLETE, REFUND_CANCEL -> {
            }
            case LOGIN, LOGOUT -> {
                User curUser = (User) target;
                name = curUser.getUsername();
                user = curUser;
            }
        }

        type.apply(name, value);
        String description = type.getDescriptionMsg();

        SystemLog log = new SystemLog(type, description, user);
        return log;
    }

    // UserTraceLogging 매개변수 일반화 작업
    public static TraceOfUserLog userLogging(ActionType type, Object target) {
        String name = "";
        User user = null;
        Object value = null;
        switch (type) {
            case BOOK_PURCHASE -> {
            }
        }
        
        type.apply(name, value);
        String description = type.getDescriptionMsg();
        TraceOfUserLog userLog = new TraceOfUserLog(type, description, user);
        return userLog;
    }

    public static SystemLog Logging(ActionType type, User user, String name) {
        type.apply(name, null);
        String description = type.getDescriptionMsg();

        SystemLog log = new SystemLog(type, description, user);
        return log;
    }

    public static SystemLog Logging(ActionType type, User user, String name, Object value) {
        type.apply(name, value);
        String description = type.getDescriptionMsg();

        SystemLog log = new SystemLog(type, description, user);
        return log;
    }

    public static TraceOfUserLog userLogging(ActionType type, User user, String name) {
        type.apply(name,null);
        String description = type.getDescriptionMsg();
        TraceOfUserLog userLog = new TraceOfUserLog(type, description, user);
        return userLog;
    }

    public static TraceOfUserLog userLogging(ActionType type, User user, String name, String objectName, Object value) {
        type.apply(name, value);
        String description = type.getDescriptionMsg();
        TraceOfUserLog userLog = new TraceOfUserLog(type, description, user);
        return userLog;
    }


}
