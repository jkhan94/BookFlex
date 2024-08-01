import React, { useState, useEffect } from 'react';
import axiosInstance from '../../api/axiosInstance';
import { useParams, useNavigate } from 'react-router-dom';
import styles from './OrderPage.module.css';

const OrderPage = () => {
    const { orderId } = useParams();
    const navigate = useNavigate();
    const [order, setOrder] = useState(null);
    const [coupons, setCoupons] = useState([]);
    const [selectedCoupon, setSelectedCoupon] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // 할인 금액 계산 함수
    const calculateDiscountedTotal = () => {
        if (!selectedCoupon || !order) return order.total;

        if (selectedCoupon.discountType === 'FIXED_AMOUNT') {
            return Math.max(0, order.total - selectedCoupon.discountPrice);
        } else if (selectedCoupon.discountType === 'PERCENTAGE') {
            return Math.max(0, order.total - (order.total * (selectedCoupon.discountPrice / 100)));
        }
        return order.total;
    };

    // 쿠폰 선택 핸들러
    const handleCouponSelect = (coupon) => {
        if (order && order.total >= coupon.minPrice) {
            setSelectedCoupon(coupon);
        } else {
            setSelectedCoupon(null);
        }
    };

    // 주문 정보 가져오기
    useEffect(() => {
        const fetchOrder = async () => {
            try {
                const response = await axiosInstance.get(`/orders/${orderId}`);
                setOrder(response.data);
            } catch (error) {
                setError(`주문 정보를 가져오는 데 오류가 발생했습니다: ${error.response ? error.response.data : error.message}`);
                console.error('Error fetching order:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchOrder();
    }, [orderId]);

    // 쿠폰 정보 가져오기
    useEffect(() => {
        const fetchCoupons = async () => {
            try {
                const response = await axiosInstance.get('/coupons/order');
                setCoupons(response.data);
            } catch (error) {
                setError(`쿠폰 정보를 가져오는 데 오류가 발생했습니다: ${error.response ? error.response.data : error.message}`);
                console.error('Error fetching coupons:', error);
            }
        };

        fetchCoupons();
    }, []);

    // 결제 핸들러\]

    const handlePayment = async () => {
        try {
            const response = await axiosInstance.post('/orders/payment', {
                orderId: order.orderId

                ,
                userCouponId: selectedCoupon ? selectedCoupon.userCouponId : null
            });

            // 결제 정보와 함께 /checkout 페이지로 이동
            navigate('/checkout', { state: response.data });
        } catch (error) {
            setError(`결제 처리 중 오류가 발생했습니다: ${error.response ? error.response.data : error.message}`);
            console.error('Error processing payment:', error);
        }
    };

    if (loading) return <div>Loading...</div>;
    if (error) return <div>{error}</div>;
    if (!order) return <div>No order found.</div>;

    return (
        <div className={styles.container}>
            <div className={styles.orderContainer}>
                <h1>주문 상세</h1>
                {order.orderItemResponseDtoList && order.orderItemResponseDtoList.length > 0 ? (
                    order.orderItemResponseDtoList.map(item => (
                        <div key={item.orderItemId} className={styles.orderItem}>
                            <img src={item.photoImagePath || ''} alt={item.bookName} className={styles.itemImage} />
                            <div className={styles.itemDetails}>
                                <div className={styles.itemName}>{item.bookName}</div>
                                <div className={styles.itemPrice}>
                                    ₩{item.price?.toLocaleString() || '0'} x {item.quantity}
                                </div>
                            </div>
                        </div>
                    ))
                ) : (
                    <div>주문 항목이 없습니다.</div>
                )}
                <div className={styles.total}>총 금액: ₩{order.total?.toLocaleString() || '0'}</div>
                <div className={styles.finalTotal}>할인된 금액: ₩{calculateDiscountedTotal().toLocaleString()}</div>

                <h2>사용 가능한 쿠폰</h2>
                {coupons.length > 0 ? (
                    <ul className={styles.couponList}>
                        {coupons.map(coupon => (
                            <li key={coupon.userCouponId} className={styles.couponItem}>
                                <input
                                    type="checkbox"
                                    id={`coupon-${coupon.userCouponId}`}
                                    disabled={order.total < coupon.minPrice}
                                    checked={selectedCoupon?.userCouponId === coupon.userCouponId}
                                    onChange={() => handleCouponSelect(coupon)}
                                />
                                <label htmlFor={`coupon-${coupon.userCouponId}`} className={`${order.total < coupon.minPrice ? styles.couponDisabled : ''}`}>
                                    <div className={styles.couponName}>{coupon.couponName}</div>
                                    <div className={styles.couponDetails}>
                                        {coupon.discountType === 'FIXED_AMOUNT' ? (
                                            <>₩{coupon.discountPrice?.toLocaleString() || '0'} 일정 금액 할인</>
                                        ) : (
                                            <>{coupon.discountPrice?.toFixed(2) || '0'}% 일정 비율 할인</>
                                        )}
                                        <span className={styles.couponMinPrice}>최소 금액: ₩{coupon.minPrice?.toLocaleString() || '0'}</span>
                                    </div>
                                </label>
                            </li>
                        ))}
                    </ul>
                ) : (
                    <div>사용 가능한 쿠폰이 없습니다.</div>
                )}

                <button className={styles.payBtn} onClick={handlePayment}>결제하기</button>
            </div>
        </div>
    );
};

export default OrderPage;
