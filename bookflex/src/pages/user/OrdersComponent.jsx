import React, { useState, useEffect } from 'react';
import axiosInstance from "../../api/axiosInstance";
import { Link } from 'react-router-dom'; // Link 임포트
import styles from './OrdersComponent.module.css';
import { useNavigate } from 'react-router-dom';

const orderStateLabels = {
    PENDING_PAYMENT: '결제대기',
    ITEM_PREPARING: '상품준비',
    IN_DELIVERY: '배송중',
    DELIVERY_COMPLETED: '배송완료',
    SALE_COMPLETED: '판매완료',
    ORDER_CANCELLED: '주문취소',
    REFUND_PROCESSING: '환불처리'
};

const OrdersComponent = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchOrders = async () => {
            try {
                const response = await axiosInstance.get('/orders');
                setOrders(response.data.content); // 데이터에서 'content' 필드만 추출
                console.log('Fetched orders:', response.data.content); // 데이터 확인
            } catch (error) {
                setError(error);
                console.error('Error fetching orders:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchOrders();
    }, []);

    const getOrderStateLabel = (state) => {
        return orderStateLabels[state] || state;
    };

    const handleOrderClick = (orderId) => {
        navigate(`/main/order/${orderId}`);
    };

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error loading orders: {error.message}</p>;

    return (
        <div>
            {orders.length === 0 ? (
                <p>No orders found.</p>
            ) : (
                <div>
                    {orders.map(order => (
                        <div key={order.orderId} className={styles.orderCard}>
                            <div className={styles.orderHeader}>
                                <div className={styles.orderHeaderTop}>
                                    <span className={styles.orderNo}>No: {order.orderNo}</span>
                                    <span className={styles.orderName}> {order.orderName}</span>
                                    <span className={styles.orderStatus}>{getOrderStateLabel(order.orderState)}</span>
                                </div>
                                <div className={styles.orderHeaderBottom}>
                                    <span className={styles.orderDate}>{new Date(order.createdAt).toLocaleDateString()}</span>
                                    <span className={styles.orderTotal}>Total: ${order.total.toFixed(2)}</span>
                                </div>
                            </div>
                            <div className={styles.orderItems}>
                                <h3>No: {order.orderNo}</h3>
                                <ul>
                                    {order.orderItemList.map(item => (
                                        <li key={item.orderItemId} className={styles.orderItem}>
                                            <img src={item.photoImagePath} alt={item.bookName} />
                                            <p>{item.bookName} - ${item.price.toFixed(2)} x {item.quantity}</p>
                                            {order.orderState === 'SALE_COMPLETED' && (
                                                <button
                                                    className={`${styles.reviewButton} ${item.reviewed ? styles.disabledButton : ''}`}
                                                    onClick={() => {
                                                        if (!item.reviewed) {
                                                            window.location.href = `/main/register-review?orderId=${order.orderId}&itemId=${item.orderItemId}`;
                                                        }
                                                    }}
                                                    disabled={item.reviewed}
                                                >
                                                    {item.reviewed ? '리뷰 작성됨' : '리뷰 작성'}
                                                </button>
                                            )}
                                        </li>
                                    ))}
                                </ul>
                                {order.orderState === 'PENDING_PAYMENT' && (
                                    <button
                                        className={styles.navigateButton}
                                        onClick={() => handleOrderClick(order.orderId)}
                                    >
                                        결제하기
                                    </button>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default OrdersComponent;
