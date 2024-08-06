import React, {useState, useEffect} from 'react';
import axiosInstance from "../../api/axiosInstance";
import './SaleReportByBookNamePage.css';
import {useNavigate} from "react-router-dom";

const CategorySaleReportPage = () => {
    const [products, setProducts] = useState([]);
    const [mainCategories, setMainCategories] = useState([]);
    const [subCategories, setSubCategories] = useState([]);
    const [selectedMainCategory, setSelectedMainCategory] = useState('');
    const [selectedSubCategory, setSelectedSubCategory] = useState('');
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [totalSaleVolume, setTotalSaleVolume] = useState(0);
    const [direction, setDirection] = useState(true);
    const [sortBy, setSortBy] = useState('bookName');
    const navigate = useNavigate();

    useEffect(() => {
        fetchMainCategories();
    }, []);

    const fetchMainCategories = async () => {
        try {
            const response = await axiosInstance.get('/categories/main');
            setMainCategories(response.data);
        } catch (error) {
            console.error('Error fetching main categories:', error);
        }
    };

    const fetchSubCategories = async (mainCategory) => {
        try {
            const response = await axiosInstance.get('/categories/sub', {
                params: {mainCategory}
            });
            setSubCategories(response.data);
        } catch (error) {
            console.error('Error fetching sub categories:', error);
        }
    };

    const fetchSalesData = async () => {
        try {
            const response = await axiosInstance.get('/sales/byCategoryName', {
                params: {
                    page,
                    size: 5,
                    direction,
                    sortBy,
                    categoryName: selectedSubCategory,
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
        if (selectedMainCategory) {
            fetchSubCategories(selectedMainCategory);
        } else {
            setSubCategories([]);
            setSelectedSubCategory('');
        }
    }, [selectedMainCategory]);

    useEffect(() => {
        fetchSalesData();
    }, [page, selectedSubCategory, startDate, endDate, direction, sortBy]);

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
            <button onClick={() => navigate('/admin/salereport-bybookname')} className="navigate-button">
                상품별 조회
            </button>
            <h1>카테고리별 매출 내역</h1>

            <form className="search-form" onSubmit={handleSearch}>
                <div className="form-group">
                    <label htmlFor="main-category">메인 카테고리</label>
                    <select
                        id="main-category"
                        name="main-category"
                        value={selectedMainCategory}
                        onChange={(e) => setSelectedMainCategory(e.target.value)}
                    >
                        <option value="">선택 안함</option>
                        {mainCategories.map((category) => (
                            <option key={category.id} value={category.name}>
                                {category.categoryName}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="sub-category">서브 카테고리</label>
                    <select
                        id="sub-category"
                        name="sub-category"
                        value={selectedSubCategory}
                        onChange={(e) => setSelectedSubCategory(e.target.value)}
                        disabled={!selectedMainCategory}
                    >
                        <option value="">서브 카테고리를 선택하세요</option>
                        {subCategories.map((category) => (
                            <option key={category.id} value={category.name}>
                                {category.categoryName}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>정렬 기준</label>
                    <div>
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
                    <th>카테고리</th>
                    <th>판매 수량</th>
                    <th>매출 합계</th>
                </tr>
                </thead>
                <tbody>
                {products.map((product, index) => (
                    <tr key={index}>
                        <td>{product.categoryName}</td>
                        <td>{product.quantity.toLocaleString()}</td>
                        <td>{product.totalPrice.toLocaleString()}원</td>
                    </tr>
                ))}
                </tbody>
                <tfoot>
                <tr>
                    <td colSpan="2" style={{textAlign: 'right'}}>전체 매출 합계:</td>
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

export default CategorySaleReportPage;
