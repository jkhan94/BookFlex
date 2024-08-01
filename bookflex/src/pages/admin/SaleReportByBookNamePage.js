// SaleReportByBookNamePage.js
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from "../../api/axiosInstance";
import './SaleReportByBookNamePage.css';

const SaleReportByBookNamePage = () => {
    const [products, setProducts] = useState([]);
    const [bookName, setBookName] = useState('');
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [totalSaleVolume, setTotalSaleVolume] = useState(0);
    const [direction, setDirection] = useState(true);
    const [sortBy, setSortBy] = useState('bookName');
    const navigate = useNavigate();

    const fetchSalesData = async () => {
        try {
            const response = await axiosInstance.get('/sales/bybookname', {
                params: {
                    page,
                    size: 5,
                    direction,
                    sortBy,
                    bookName,
                    startDate,
                    endDate,
                }
            });

            setProducts(response.data.data.saleVolumeRowList.content);
            setTotalPages(response.data.data.saleVolumeRowList.totalPages);
            setTotalSaleVolume(response.data.data.inquiryTotalSaleVolume.totalSaleVolume);
            console.log(products);
        } catch (error) {
            console.error('Error fetching sales data:', error);
        }
    };

    useEffect(() => {
        fetchSalesData();
    }, [page, bookName, startDate, endDate, direction, sortBy]);

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
            <button onClick={() => navigate('/admin/salereport-bycategory')} className="navigate-button">
                카테고리별 조회
            </button>
            <h1>상품별 매출 내역</h1>
            <form className="search-form" onSubmit={handleSearch}>
                <div className="form-group">
                    <label htmlFor="book-name">상품명</label>
                    <input
                        type="text"
                        id="book-name"
                        name="book-name"
                        value={bookName}
                        onChange={(e) => setBookName(e.target.value)}
                        placeholder="상품명을 입력하세요"
                    />
                </div>

                <div className="form-group">
                    <label>정렬 기준</label>
                    <div>
                        <label>
                            상품명
                            <input
                                type="radio"
                                name="sort-by"
                                value="bookName"
                                checked={sortBy === 'bookName'}
                                onChange={handleSortChange}
                            />
                        </label>
                        <label>
                            매출합계
                            <input
                                type="radio"
                                name="sort-by"
                                value="total"
                                checked={sortBy === 'total'}
                                onChange={handleSortChange}
                            />
                        </label>
                    </div>
                </div>

                <div className="form-group">
                    <label htmlFor="sort-order">정렬 순서</label>
                    <select
                        id="sort-order"
                        name="sort-order"
                        value={direction}
                        onChange={(e) => setDirection(e.target.value === 'true')}
                    >
                        <option value="true">오름차순</option>
                        <option value="false">내림차순</option>
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
                    <th>이미지</th>
                    <th>상품명</th>
                    <th>출판사</th>
                    <th>카테고리</th>
                    <th>판매 수량</th>
                    <th>매출 합계</th>
                </tr>
                </thead>
                <tbody>
                {products.map((product, index) => (
                    <tr key={index}>
                        <td><img src={product.image} alt={product.bookName} className="product-image"/></td>
                        <td>{product.bookName}</td>
                        <td>{product.publisher}</td>
                        <td>{product.categoryName}</td>
                        <td>{product.quantity.toLocaleString()}</td>
                        <td>{product.totalPrice.toLocaleString()}원</td>
                    </tr>
                ))}
                </tbody>
                <tfoot>
                <tr>
                    <td colSpan="5" style={{textAlign: 'right'}}>전체 매출 합계:</td>
                    <td>{totalSaleVolume.toLocaleString()}원</td>
                </tr>
                </tfoot>
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
