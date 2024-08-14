import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from "../../api/axiosInstance";
import './SaleReportByBookNamePage.css';
import styles from "../user/OrdersComponent.module.css";
import { isAdmin } from './tokenCheck';

const OrderHistory = () => {
    const [orders, setOrders] = useState([]);
    const [username, setUsername] = useState('');
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [status, setStatus] = useState(''); // 결제 상태 필터
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [direction, setDirection] = useState(true); // isAsc 파라미터에 대응
    const [sortBy, setSortBy] = useState('createdAt'); // 기본 정렬 기준은 createdAt으로 설정
    const navigate = useNavigate();

    useEffect(() => {
        if(!isAdmin())
            navigate('/main/dashboard');
    },[navigate]);

    // 서버에서 데이터를 가져오는 함수
    const fetchOrders = async () => {
        try {
            const response = await axiosInstance.get('/orders', {
                params: {
                    status: status || '',  // 필터된 상태
                    page: page - 1,        // 서버는 0-based index를 사용할 수 있음
                    size: 10,              // 페이지당 항목 수
                    sort: `${sortBy},${direction ? 'asc' : 'desc'}`, // 정렬 기준과 방향
                    startDate: startDate || '19000101',  // 기본 시작 날짜
                    endDate: endDate || '99991231',
                    username: username,// 기본 종료 날짜
                },
            });

            setOrders(response.data.content);
            setTotalPages(response.data.totalPages);
            console.log('Fetched orders:', response.data.content);
        } catch (error) {
            console.error('Error fetching orders:', error);
        }
    };

    useEffect(() => {
        fetchOrders();
    }, [status, page, sortBy, direction, startDate, endDate]);

    const handleSearch = (e) => {
        e.preventDefault();
        setPage(1); // 검색 시 페이지를 1로 초기화
        fetchOrders();
    };

    const handlePageChange = (newPage) => {
        if (newPage >= 1 && newPage <= totalPages) {
            setPage(newPage);
        }
    };

    const handleSortChange = (e) => {
        setSortBy(e.target.value);
    };

    const handleStatusChange = (e) => {
        setStatus(e.target.value);
    };

    const convertOrderState = (state) => {
        switch (state) {
            case 'PENDING_PAYMENT':
                return '결제 대기 중';
            case 'ITEM_PREPARING':
                return '상품 준비 중';
            case 'IN_DELIVERY':
                return '배송 중';
            case 'DELIVERY_COMPLETED':
                return '배송 완료';
            case 'SALE_COMPLETED':
                return '판매 완료';
            case 'ORDER_CANCELLED':
                return '주문 취소';
            case 'REFUND_PROCESSING':
                return '환불 처리 중';
            default:
                return state;
        }
    };

    return (
        <div className="container">
            <h1>주문 내역 조회</h1>
            <form className="search-form" onSubmit={handleSearch}>
                <div className="form-group">
                    <label htmlFor="username">주문자명</label>
                    <input
                        type="text"
                        id="username"
                        name="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="주문자명을 입력하세요"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="status">결제 상태</label>
                    <select
                        id="status"
                        name="status"
                        value={status}
                        onChange={handleStatusChange}
                    >
                        <option value="">전체</option>
                        <option value="PENDING_PAYMENT">결제 대기 중</option>
                        <option value="ITEM_PREPARING">상품 준비 중</option>
                        <option value="IN_DELIVERY">배송 중</option>
                        <option value="DELIVERY_COMPLETED">배송 완료</option>
                        <option value="SALE_COMPLETED">판매 완료</option>
                        <option value="ORDER_CANCELLED">주문 취소</option>
                        <option value="REFUND_PROCESSING">환불 처리 중</option>
                    </select>
                </div>

                <div className="form-group">
                    <label>판매 기간</label>
                    <div className="date-range">
                        <input
                            type="date"
                            id="start-date"
                            name="start-date"
                            value={startDate}
                            onChange={(e) => setStartDate(e.target.value)}
                        />
                        <input
                            type="date"
                            id="end-date"
                            name="end-date"
                            value={endDate}
                            onChange={(e) => setEndDate(e.target.value)}
                        />
                    </div>
                </div>

                <button type="submit" className="search-button">조회</button>
            </form>

            <table>
                <thead>
                <tr>
                    <th>주문번호</th>
                    <th>주문일(결제일)</th>
                    <th>주문자</th>
                    <th>상품명</th>
                    <th>상품 내역</th>
                    <th>총 결제금액</th>
                    <th>결제상태</th>
                </tr>
                </thead>
                <tbody>
                {orders.map((order) => (
                    <tr key={order.orderId}>
                        <td>{order.orderNo}</td>
                        <td>{new Date(order.createdAt).toLocaleString()}</td>
                        <td>{order.username}</td>
                        <td>{order.orderName}</td>
                        <td>
                            <ul>
                                {order.orderItemList.map(item => (
                                    <li key={item.orderItemId} className={styles.orderItem}>
                                        <p>{item.bookName} - {item.price.toFixed(2)} 원 x {item.quantity} 권</p>
                                    </li>
                                ))}
                            </ul>
                        </td>
                        <td>{order.total} 원</td>
                        <td>
                            <button
                                className={`status-button ${order.orderState === 'SALE_COMPLETED' ? 'status-completed' : 'status-processing'}`}
                            >
                                {convertOrderState(order.orderState)}
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            <div className="pagination">
                <button
                    onClick={() => handlePageChange(page - 1)}
                    disabled={page === 1}
                >
                    이전
                </button>
                <span>페이지 {page} / {totalPages}</span>
                <button
                    onClick={() => handlePageChange(page + 1)}
                    disabled={page === totalPages}
                >
                    다음
                </button>
            </div>
        </div>
    );
};

export default OrderHistory;
