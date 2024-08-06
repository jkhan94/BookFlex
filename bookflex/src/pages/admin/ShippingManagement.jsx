import React, { useState, useEffect } from 'react';
import axiosInstance from "../../api/axiosInstance";
import styles from './ShippingManagement.module.css';

const ShippingManagement = () => {
    const [orders, setOrders] = useState([]);
    const [shipinfos, setShipInfos] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [ordersPerPage, setOrdersPerPage] = useState(5);
    const [totalCount, setTotalCount] = useState(0);

    const totalPages = Math.ceil(totalCount / ordersPerPage);

    const getOrders = async (page, size) => {
        try {
            const response = await axiosInstance.get('/orders', {
                params: {
                    page: page,
                    size: size
                }
            });
            const { totalCount, orderShipResDtos } = response.data;
            setTotalCount(totalCount)
            setOrders(orderShipResDtos.content);
        } catch (error) {
            console.error("주문정보 불러오기 실패", error);
        }
    };

    const getShipInfo = async (orders) => {
        const shipInfoPromises = orders.map(order =>
            axiosInstance.post('/shipments', {
                trackingNumber: order.trackingNumber,
                carrier: order.carrier
            }).then(response => ({
                orderNumber: order.orderNumber,
                ...response.data
            }))
        );

        try {
            const shipInfoResults = await Promise.all(shipInfoPromises);
            setShipInfos(shipInfoResults);
        } catch (error) {
            console.error("배송정보 불러오기 실패", error);
        }
    };

    const handleSearch = () => {
        setCurrentPage(1);
    };

    const handlePrevPage = () => {
        setCurrentPage(prev => Math.max(prev - 1, 1));
    };

    const handleNextPage = () => {
        setCurrentPage(prev => Math.min(prev + 1, totalPages));
    };

    useEffect(() => {
        const fetchData = async () => {
            await getOrders(currentPage, ordersPerPage);
        };
        fetchData();
    }, [currentPage, ordersPerPage]);

    useEffect(() => {
        if (orders.length > 0) {
            getShipInfo(orders);
        }
    }, [orders]);

    return (
        <div>
            <h1>배송정보 페이지</h1>
            <div className={styles.searchContainer}>
                <input
                    type="text"
                    id="search-input"
                    className={styles.searchInput}
                    placeholder="Search by order number or username..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
                <button
                    id="search-button"
                    className={styles.searchButton}
                    onClick={handleSearch}
                >
                    Search
                </button>
            </div>
            <table id="order-list" className={styles.orderList}>
                <thead>
                <tr>
                    <th>주문번호</th>
                    <th>유저ID</th>
                    <th>운송시작일</th>
                    <th>배달시작일</th>
                    <th>현재 배송상태</th>
                    <th>배송회사명</th>
                </tr>
                </thead>
                <tbody id="order-body">
                {orders.map(order => {
                    const shipInfo = shipinfos.find(info => info.orderNumber === order.orderNumber) || {};
                    return (
                        <tr key={order.orderNumber}>
                            <td>{order.orderNumber}</td>
                            <td>{order.username}</td>
                            <td>{shipInfo.shippedAt || 'N/A'}</td>
                            <td>{shipInfo.deliveredAt || 'N/A'}</td>
                            <td>{shipInfo.status || 'N/A'}</td>
                            <td>{shipInfo.carrierName || 'N/A'}</td>
                        </tr>
                    );
                })}
                </tbody>
            </table>
            <div id="pagination" className={styles.pagination}>
                <button
                    id="prev-page"
                    onClick={handlePrevPage}
                    disabled={currentPage === 1}
                >
                    Previous
                </button>
                <span id="page-info" className={styles.pageInfo}>
                    Page {currentPage} of {totalPages}
                </span>
                <button
                    id="next-page"
                    onClick={handleNextPage}
                    disabled={currentPage === totalPages}
                >
                    Next
                </button>
            </div>
        </div>
    );
};

export default ShippingManagement;
