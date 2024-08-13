import React, { useEffect, useState } from 'react';
import axiosInstance from "../../api/axiosInstance";
import styles from './ShippingManagement.module.css';

const ShippingManagement = () => {
    const [shipInfos, setShipInfos] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [shipInfoPerPage, setShipInfoPerPage] = useState(5);
    const [totalCount, setTotalCount] = useState(0);
    const [sortOrder, setSortOrder] = useState(true); // 정렬 순서 상태 추가
    const [newShipInfoPerPage, setNewShipInfoPerPage] = useState(shipInfoPerPage); // 입력된 페이지당 항목 수 상태 추가

    const totalPages = Math.ceil(totalCount / shipInfoPerPage);

    const getOrders = async (page, size, sort) => {
        try {
            const response = await axiosInstance.get('/shipments', {
                params: {
                    page: page,
                    size: size,
                    isAsc: sortOrder
                }
            });
            const { totalCount, shipmentResDtos } = response.data;
            setTotalCount(totalCount);
            setShipInfos(shipmentResDtos.content);
        } catch (error) {
            console.error("주문정보 불러오기 실패", error);
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

    const handleSortOrderChange = (e) => {
        if(e.target.value === "asc") {
            setSortOrder(true)
        }
        else {
            setSortOrder(false);
        }
    };

    const handleShipInfoPerPageChange = () => {
        setShipInfoPerPage(newShipInfoPerPage); // 새로운 페이지당 항목 수 설정
        setCurrentPage(1); // 페이지를 처음으로 리셋
    };

    useEffect(() => {
        const fetchData = async () => {
            // 페이지가 변경될 때마다 shipInfos 상태를 초기화
            setShipInfos([]);
            await getOrders(currentPage, shipInfoPerPage, sortOrder);
        };
        fetchData();
    }, [currentPage, shipInfoPerPage, sortOrder]);

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
            <div className={styles.sortContainer}>
                <label htmlFor="sortOrder">정렬 기준: </label>
                <select id="sortOrder" value={sortOrder ? "asc" : "desc"} onChange={handleSortOrderChange}>
                    <option value="asc">오름차순</option>
                    <option value="desc">내림차순</option>
                </select>
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
                {shipInfos.map(shipInfo => (
                    <tr key={shipInfo.orderNo}>
                        <td>{shipInfo.orderNo}</td>
                        <td>{shipInfo.username}</td>
                        <td>{shipInfo.shippedAt || 'N/A'}</td>
                        <td>{shipInfo.deliveredAt || 'N/A'}</td>
                        <td>{shipInfo.status || 'N/A'}</td>
                        <td>{shipInfo.carrierName || 'N/A'}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            <div className={styles.pageSizeContainer}>
                <label htmlFor="page-size">페이지당 항목 수: </label>
                <input
                    id="page-size"
                    type="number"
                    value={newShipInfoPerPage}
                    onChange={(e) => setNewShipInfoPerPage(Number(e.target.value))}
                />
                <button onClick={handleShipInfoPerPageChange}>적용</button>
            </div>
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
