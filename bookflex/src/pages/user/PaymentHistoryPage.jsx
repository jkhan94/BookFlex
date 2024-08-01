import React, { useState, useEffect } from 'react';
import moment from 'moment';
import './PaymentHistoryPage.css';

const PaymentHistoryPage = () => {
    const ordersData = [
        { id: 'ORD-001', date: '2023-05-15', product: '스마트폰', price: 800000, paymentStatus: 'completed', shippingStatus: 'delivered' },
        { id: 'ORD-002', date: '2023-05-10', product: '노트북', price: 1500000, paymentStatus: 'pending', shippingStatus: 'processing' },
        { id: 'ORD-003', date: '2023-05-05', product: '무선이어폰', price: 200000, paymentStatus: 'completed', shippingStatus: 'shipped' },
        { id: 'ORD-004', date: '2023-04-28', product: '스마트워치', price: 300000, paymentStatus: 'completed', shippingStatus: 'delivered' },
        { id: 'ORD-005', date: '2023-04-20', product: '태블릿', price: 600000, paymentStatus: 'failed', shippingStatus: 'cancelled' },
    ];

    const [orders, setOrders] = useState(ordersData);
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        const filteredOrders = ordersData.filter(order =>
            order.id.toLowerCase().includes(searchTerm.toLowerCase()) ||
            order.product.toLowerCase().includes(searchTerm.toLowerCase())
        );
        setOrders(filteredOrders);
    }, [searchTerm]);

    const getPaymentStatusText = (status) => {
        switch(status) {
            case 'completed': return '결제 완료';
            case 'pending': return '결제 대기';
            case 'failed': return '결제 실패';
            default: return '알 수 없음';
        }
    };

    const getShippingStatusText = (status) => {
        switch(status) {
            case 'processing': return '처리 중';
            case 'shipped': return '배송 중';
            case 'delivered': return '배송 완료';
            case 'cancelled': return '취소됨';
            default: return '알 수 없음';
        }
    };

    return (
        <div className="container">
            <h1>주문내역 조회</h1>

            <div className="search-bar">
                <input
                    type="text"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    placeholder="주문번호 또는 상품명으로 검색"
                />
                <button onClick={() => {}}>검색</button>
            </div>

            <ul className="order-list">
                {orders.length === 0 ? (
                    <p className="no-results">검색 결과가 없습니다.</p>
                ) : (
                    orders.map(order => (
                        <li key={order.id} className="order-item">
                            <div className="order-header">
                                <span className="order-number">{order.id}</span>
                                <span className="order-date">{moment(order.date).format('YYYY년 MM월 DD일')}</span>
                            </div>
                            <div className="order-details">
                                <div className="order-product">
                                    <strong>{order.product}</strong>
                                    <p>{order.price.toLocaleString()}원</p>
                                </div>
                                <div className="order-status">
                                    <div className={`status-badge payment-status ${order.paymentStatus}`}>
                                        {getPaymentStatusText(order.paymentStatus)}
                                    </div>
                                    <div className={`status-badge shipping-status ${order.shippingStatus}`}>
                                        {getShippingStatusText(order.shippingStatus)}
                                    </div>
                                </div>
                            </div>
                        </li>
                    ))
                )}
            </ul>
        </div>
    );
};

export default PaymentHistoryPage;
