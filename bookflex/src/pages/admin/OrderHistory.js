import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import axiosInstance from "../../api/axiosInstance";
import './SaleReportByBookNamePage.css';

const SaleReportByBookNamePage = () => {
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

    const fetchSalesData = async () => {
        try {
            const response = await axiosInstance.get('/sales/admin', {
                params: {
                    page,
                    size: 10, // 한번에 가져올 데이터의 수
                    direction, // 오름차순 / 내림차순 정렬 여부
                    sortBy, // 정렬 기준
                    username, // 주문자명
                    status, // 결제 상태 필터
                    startDate: startDate ? startDate.replace(/-/g, '') : '19000101', // API에 맞게 날짜 형식을 변환
                    endDate: endDate ? endDate.replace(/-/g, '') : '99991231', // API에 맞게 날짜 형식을 변환
                }
            });
            setOrders(response.data.data.content);
            setTotalPages(response.data.data.totalPages);
        } catch (error) {
            console.error('Error fetching sales data:', error);
        }
    };

    useEffect(() => {
        fetchSalesData();
    }, [page, username, status, startDate, endDate, direction, sortBy]);

    const handleSearch = (e) => {
        e.preventDefault();
        setPage(1); // 검색 시 페이지를 1로 초기화
        fetchSalesData();
    };

    const handlePageChange = (newPage) => {
        if (newPage >= 1 && newPage <= totalPages) {
            setPage(newPage);
        }
    };

    const handleSortChange = (e) => {
        setSortBy(e.target.value);
    };

    return (
        <div className="container">
            <h1>상품별 매출 내역</h1>
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
                        onChange={(e) => setStatus(e.target.value)}
                    >
                        <option value="">전체</option>
                        <option value="pendingPayment">결제 대기 중</option>
                        <option value="itemPrepairng">상품 준비 중</option>
                        <option value="inDelivery">배송 중</option>
                        <option value="deliveryCompleted">배송 완료</option>
                        <option value="saleCompleted">판매 완료</option>
                        <option value="orderCancelled">주문 취소</option>
                        <option value="refundProcessing">환불 처리 중</option>
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
                    <th>가격</th>
                    <th>수량</th>
                    <th>총 결제금액</th>
                    <th>결제상태</th>
                </tr>
                </thead>
                <tbody>
                {orders.map((order, index) => (
                    <tr key={index}>
                        <td>{order.saleId}</td>
                        <td>{new Date(order.createdAt).toLocaleString()}<br/></td>
                        <td>{order.username}</td>
                        <td>{order.bookName}</td>
                        <td>{order.price} 원</td>
                        <td>{order.quantity} 권</td>
                        <td>{order.totalAmount} 원</td>
                        <td>
                            <button
                                className={`status-button ${order.orderState === '결제완료' ? 'status-completed' : 'status-processing'}`}>
                                {order.orderState}
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

export default SaleReportByBookNamePage;
