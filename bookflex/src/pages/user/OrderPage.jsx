import React, { useState, useEffect } from 'react';
import axiosInstance from '../../api/axiosInstance'; // axios 인스턴스 임포트
import { useParams } from 'react-router-dom';
import styles from './OrderPage.module.css'; // CSS 모듈 임포트

const OrderPage = () => {
    const { orderId } = useParams(); // URL 파라미터에서 orderId 가져오기
    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchOrder = async () => {
            try {
                const response = await axiosInstance.get(`/orders/${orderId}`);
                setOrder(response.data);
            } catch (error) {
                setError('주문 정보를 가져오는 데 오류가 발생했습니다.');
                console.error('Error fetching order:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchOrder();
    }, [orderId]);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>{error}</div>;

    if (!order) return <div>No order found.</div>;

    return (
        <div className={styles.container}>
            <div className={styles.orderContainer}>
                <h1>주문 상세</h1>
                {order.orderItemResponseDtoList.map(item => (
                    <div key={item.orderItemId} className={styles.orderItem}>
                        <img src={item.photoImagePath} alt={item.bookName} className={styles.itemImage} />
                        <div className={styles.itemDetails}>
                            <div className={styles.itemName}>{item.bookName}</div>
                            <div className={styles.itemPrice}>₩{item.price.toLocaleString()} x {item.quantity}</div>
                        </div>
                    </div>
                ))}
                <div className={styles.total}>총 금액: ₩{order.total.toLocaleString()}</div>
                <div className={styles.finalTotal}>주문 상태: {order.status}</div>
                <button className={styles.payBtn} onClick={() => alert('결제 완료')}>결제하기</button>
            </div>
        </div>
    );
};

export default OrderPage;
