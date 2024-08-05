import React, { useEffect, useRef, useState } from "react";
// import { loadPaymentWidget, ANONYMOUS } from "@tosspayments/payment-widget-sdk";
import { nanoid } from "nanoid";
import { useLocation } from 'react-router-dom';

// 구매자의 고유 아이디를 불러와서 customerKey로 설정하세요.
// 이메일・전화번호와 같이 유추가 가능한 값은 안전하지 않습니다.
const widgetClientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm";
const customerKey = "fhFs08TibVLX7la1QTuA8";
// const paymentWidget = PaymentWidget(widgetClientKey, PaymentWidget.ANONYMOUS) // 비회원 결제

export function CheckoutPage() {
    const location = useLocation();
    const paymentData = location.state;
    const [paymentWidget, setPaymentWidget] = useState(null);
    const paymentMethodsWidgetRef = useRef(null);
    const [paymentInfo, setPaymentInfo] = useState({
        currency: "KRW",
        price: paymentData ? paymentData.paymentAmount : 0,
        orderNo: paymentData ? paymentData.orderNo : nanoid(),
        orderName : paymentData.orderName,
        customerEmail : paymentData.customerEmail,
        customerName : paymentData.customerName,
        customerMobilePhone : paymentData.customerMobilePhone,
        successUrl: `${window.location.origin}/success`,
        failUrl: `${window.location.origin}/fail`

    });



    useEffect(() => {
        const fetchPaymentWidget = async () => {
            try {
                const loadedWidget = await loadPaymentWidget(widgetClientKey, customerKey);
                setPaymentWidget(loadedWidget);
            } catch (error) {
                console.error("Error fetching payment widget:", error);
            }
        };

        fetchPaymentWidget();
    }, []);

    useEffect(() => {
        if (paymentWidget == null) {
            return;
        }

        const paymentMethodsWidget = paymentWidget.renderPaymentMethods(
            "#payment-widget",
            { value: paymentInfo.price },
            { variantKey: "DEFAULT" }
        );

        paymentWidget.renderAgreement(
            "#agreement",
            { variantKey: "AGREEMENT" }
        );

        paymentMethodsWidgetRef.current = paymentMethodsWidget;
    }, [paymentWidget, paymentInfo.price]);

    useEffect(() => {
        const paymentMethodsWidget = paymentMethodsWidgetRef.current;

        if (paymentMethodsWidget == null) {
            return;
        }

        paymentMethodsWidget.updateAmount(paymentInfo.price);
    }, [paymentInfo.price]);

    const handlePaymentRequest = async () => {
        // 결제를 요청하기 전에 orderId, amount를 서버에 저장하세요.
        // 결제 과정에서 악의적으로 결제 금액이 바뀌는 것을 확인하는 용도입니다.
        try {
            await paymentWidget?.requestPayment({
                orderId: paymentInfo.orderNo,
                orderName: paymentInfo.orderName,
                customerName: paymentInfo.customerName,
                customerEmail: paymentInfo.customerEmail,
                customerMobilePhone: paymentInfo.customerMobilePhone,
                successUrl: `${window.location.origin}/success`,
                failUrl: `${window.location.origin}/fail`,
            });
        } catch (error) {
            console.error("Error requesting payment:", error);
        }
    };

    return (
        <div>
            {/* 할인 쿠폰 */}

            {/* 결제 UI, 이용약관 UI 영역 */}
            <div id="payment-widget" />
            <div id="agreement" />
            {/* 결제하기 버튼 */}
            <button onClick={handlePaymentRequest}>결제하기</button>
        </div>
    );
}