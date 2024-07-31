import React, { useState, useEffect } from 'react';
import axiosInstance from '../../api/axiosInstance'; // axios 인스턴스 임포트
import { useParams } from 'react-router-dom';
import styles from './OrderPage.module.css'; // CSS 모듈 임포트

const OrderPage = () => {
    const { orderId } = useParams(); // URL 파라미터에서 orderId 가져오기
    const [order, setOrder] = useState(null);
    const [coupons, setCoupons] = useState([]); // 쿠폰 상태 추가
    const [selectedCoupon, setSelectedCoupon] = useState(null); // 선택된 쿠폰 상태 추가
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // 주문 정보 가져오기
    useEffect(() => {
        const fetchOrder = async () => {
            try {
                const response = await axiosInstance.get(`/orders/${orderId}`);
                setOrder(response.data);
            } catch (error) {
                setError('주문 정보를 가져오는 데 오류가 발생했습니다.');
                console.error('Error fetching order:', error);
            } finally {
                setLoading(false); // 데이터 요청 완료 후 로딩 상태 업데이트
            }
        };

        fetchOrder();
    }, [orderId]);

    // 쿠폰 정보 가져오기
    useEffect(() => {
        const fetchCoupons = async () => {
            try {
                const response = await axiosInstance.get('/coupons/order'); // 쿠폰 API 호출 수정
                console.log(response.data);
                setCoupons(response.data);
            } catch (error) {
                setError('쿠폰 정보를 가져오는 데 오류가 발생했습니다.');
                console.error('Error fetching coupons:', error);
            }
        };

        fetchCoupons();
    }, []);

    const handleCouponSelect = (coupon) => {
        if (selectedCoupon === coupon) {
            // 이미 선택된 쿠폰을 클릭하면 선택 해제
            setSelectedCoupon(null);
        } else if (order && order.total >= coupon.minPrice) {
            // 조건에 맞는 쿠폰을 선택
            setSelectedCoupon(coupon);
        }
    };

    const calculateDiscountedTotal = () => {
        if (!selectedCoupon || !order) return order.total;

        if (selectedCoupon.discountType === '일정 금액 할인') {
            return Math.max(0, order.total - selectedCoupon.discountPrice);
        } else if (selectedCoupon.discountType === '일정 비율 할인') {
            return Math.max(0, order.total - (order.total * (selectedCoupon.discountPrice / 100)));
        }
        return order.total;
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
                                    checked={selectedCoupon === coupon}
                                    onChange={() => handleCouponSelect(coupon)}
                                />
                                <label htmlFor={`coupon-${coupon.userCouponId}`} className={`${order.total < coupon.minPrice ? styles.couponDisabled : ''}`}>
                                    <div className={styles.couponName}>{coupon.couponName}</div>
                                    <div className={styles.couponDetails}>
                                        {coupon.discountType === '일정 금액 할인' ? (
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

                <button className={styles.payBtn} onClick={() => alert('결제 완료')}>결제하기</button>
            </div>
        </div>
    );
};

export default OrderPage;
