import React, {useEffect, useState} from 'react';
import axiosInstance from "../../api/axiosInstance";
import styles from '../admin/couponmanagement.module.css'; // CSS 파일을 여기에 넣으세요

const PAGE_SIZE = 5;

const CouponManagement = () => {
    const [coupons, setCoupons] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [totalElements, setTotalElements] = useState(0);
    const [error, setError] = useState(null);
    const [showCreateForm, setShowCreateForm] = useState(false);
    const [newCoupon, setNewCoupon] = useState({
        couponType: '',
        couponName: '',
        validityDays: '',
        totalCount: '',
        discountType: '',
        minPrice: '',
        discountPrice: '',
        eligibleGrade: '',
        startDate: '',
        expirationDate: ''
    });

    const [couponTypes, setCouponTypes] = useState([]);
    const [discountTypes, setDiscountTypes] = useState([]);
    const [eligibleGrades, setEligibleGrades] = useState([]);

    useEffect(() => {
        fetchCoupons(currentPage);
        fetchEnumValues();
    }, [currentPage]);

    const fetchCoupons = async (page) => {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axiosInstance.get('/coupons/admin', {
                headers: {
                    Authorization: token,
                    'Content-Type': 'application/json',
                },
                params: {
                    page: page, // 서버는 0 기반 페이지 인덱스를 사용합니다
                    size: PAGE_SIZE,
                    sortBy: 'createdAt',
                }
            });

            if (response.data.content) {
                setCoupons(response.data.content);
                setTotalPages(response.data.totalPages);
                setTotalElements(response.data.totalElements);
            } else {
                throw new Error('API response does not contain expected data');
            }
        } catch (error) {
            alert('쿠폰을 불러오는 중 오류가 발생했습니다: ' + error.message);
        }
    };

    const fetchEnumValues = async () => {
        try {
            const token = localStorage.getItem('Authorization');
            const [couponTypeRes, discountTypeRes, userGradeRes] = await Promise.all([
                axiosInstance.get('/coupons/types', {
                    headers: {
                        Authorization: token,
                        'Content-Type': 'application/json',
                    }
                }),
                axiosInstance.get('/coupons/discounts', {
                    headers: {
                        Authorization: token,
                        'Content-Type': 'application/json',
                    }
                }),
                axiosInstance.get('/coupons/grades', {
                    headers: {
                        Authorization: token,
                        'Content-Type': 'application/json',
                    }
                })
            ]);
            setCouponTypes(couponTypeRes.data);
            setDiscountTypes(discountTypeRes.data);
            setEligibleGrades(userGradeRes.data);
        } catch (error) {
            alert('ENUM 값을 불러오는 중 오류가 발생했습니다: ' + error.message);
        }
    };

    const handlePreviousPage = () => {
        if (currentPage > 1) {
            setCurrentPage(currentPage - 1);
        }
    };

    const handleNextPage = () => {
        if (currentPage < totalPages) {
            setCurrentPage(currentPage + 1);
        }
    };

    const handleUpdateCount = async (couponId) => {
        const newCount = prompt('새로운 수량을 입력하세요:', '0');
        if (newCount !== null) {
            const newCountNumber = Number(newCount); // 입력값을 정수로 변환
            if (!isNaN(newCountNumber) && newCountNumber >= 0) {
                try {
                    const token = localStorage.getItem('Authorization');
                    await axiosInstance.put(`/coupons/${couponId}`, {totalCount: newCountNumber}, {
                        headers: {
                            Authorization: token,
                            'Content-Type': 'application/json',
                        }
                    });
                    fetchCoupons(currentPage); // 리스트 새로 고침
                } catch (error) {
                    alert('쿠폰 수량을 업데이트하는 중 오류가 발생했습니다: ' + error.message);
                }
            } else {
                alert('유효한 수량을 입력하세요.');
            }
        }
    };

    const handleDeleteCoupon = async (couponId) => {
        if (window.confirm('정말로 이 쿠폰을 삭제하시겠습니까?')) {
            try {
                const token = localStorage.getItem('Authorization');
                await axiosInstance.delete(`/coupons/${couponId}`, {
                    headers: {
                        Authorization: token,
                        'Content-Type': 'application/json',
                    }
                });
                fetchCoupons(currentPage); // 리스트 새로 고침
            } catch (error) {
                alert('쿠폰을 삭제하는 중 오류가 발생했습니다: ' + error.message);
            }
        }
    };

    const handleCreateCoupon = async () => {
        try {
            const token = localStorage.getItem('Authorization');
            await axiosInstance.post('/coupons', newCoupon, {
                headers: {
                    Authorization: token,
                    'Content-Type': 'application/json',
                }
            });
            fetchCoupons(currentPage); // 리스트 새로 고침
            setShowCreateForm(false);
            setNewCoupon({
                couponType: '',
                couponName: '',
                validityDays: '',
                totalCount: '',
                discountType: '',
                minPrice: '',
                discountPrice: '',
                eligibleGrade: '',
                startDate: '',
                expirationDate: ''
            });
        } catch (error) {
            alert('쿠폰을 생성하는 중 오류가 발생했습니다: ' + error.message);
        }
    };

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setNewCoupon((prevCoupon) => ({
            ...prevCoupon,
            [name]: value
        }));
    };

    const handleIssueCouponToAll = async (couponId) => {
        try {
            const token = localStorage.getItem('Authorization');
            await axiosInstance.post(`/coupons/issue/all/${couponId}`, {}, {
                headers: {
                    Authorization: token,
                    'Content-Type': 'application/json',
                }
            });
            alert('쿠폰이 모든 사용자에게 발급되었습니다.');
        } catch (error) {
            alert('쿠폰을 발급하는 중 오류가 발생했습니다: ' + error.message);
        }
    };

    return (
        <div className={styles.container}>
            <h1>쿠폰 관리</h1>
            <table className={styles.table}>
                <thead>
                <tr>
                    <th>쿠폰유형</th>
                    <th>쿠폰이름</th>
                    <th>쿠폰 유효기간</th>
                    <th>총수량</th>
                    <th>할인유형</th>
                    <th>최소주문금액</th>
                    <th>할인금액</th>
                    <th>사용가능등급</th>
                    <th>쿠폰 발급 시작일</th>
                    <th>쿠폰 발급 만료일</th>
                    <th>작업</th>
                </tr>
                </thead>
                <tbody>
                {coupons.map(coupon => (
                    <tr key={coupon.couponId}>
                        <td>{coupon.couponType}</td>
                        <td>{coupon.couponName}</td>
                        <td>{coupon.validityDays === 0 ? "쿠폰 만료일까지" : "발급 가능 기간 내에 발급일로부터 " + coupon.validityDays + "일"}</td>
                        <td>{coupon.totalCount}</td>
                        <td>{coupon.discountType}</td>
                        <td>{coupon.minPrice}</td>
                        <td>{coupon.discountPrice}</td>
                        <td>{coupon.eligibleGrade}</td>
                        <td>{coupon.startDate}</td>
                        <td>{coupon.expirationDate}</td>
                        <td>
                            <div className={styles.buttonContainer}>
                                <button className={styles.btnAction}
                                        onClick={() => handleUpdateCount(coupon.couponId)}>수량 변경
                                </button>
                                <button className={styles.btnAction}
                                        onClick={() => handleDeleteCoupon(coupon.couponId)}>삭제
                                </button>
                                <button className={styles.btnAction}
                                        onClick={() => handleIssueCouponToAll(coupon.couponId)}>쿠폰 일괄 발급
                                </button>
                            </div>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <div className={styles.pagination}>
                <button onClick={handlePreviousPage} disabled={currentPage === 1}>이전</button>
                <span>페이지 {currentPage} / {totalPages}</span>
                <button onClick={handleNextPage} disabled={currentPage === totalPages}>다음</button>
            </div>

            <div className={styles.createCouponContainer}>
                <button className={styles.btnAction} onClick={() => setShowCreateForm(!showCreateForm)}>
                    {showCreateForm ? '취소' : '쿠폰 생성'}
                </button>

                {showCreateForm && (
                    <div className={styles.createForm}>
                        <h2>새 쿠폰 생성</h2>
                        <form className={styles.createFormContent}>
                            <div className={styles.formGroup}>
                                <label>쿠폰 유형</label>
                                <select
                                    name="couponType"
                                    value={newCoupon.couponType}
                                    onChange={handleInputChange}
                                >
                                    <option value="">선택하세요</option>
                                    {couponTypes.map((type) => (
                                        <option key={type} value={type}>{type}</option>
                                    ))}
                                </select>
                            </div>
                            <div className={styles.formGroup}>
                                <label>쿠폰 이름</label>
                                <input
                                    type="text"
                                    name="couponName"
                                    value={newCoupon.couponName}
                                    onChange={handleInputChange}
                                    placeholder="쿠폰 이름"
                                />
                            </div>
                            <div className={styles.formGroup}>
                                <label>유효 기간</label>
                                <input
                                    type="number"
                                    name="validityDays"
                                    value={newCoupon.validityDays}
                                    onChange={handleInputChange}
                                    placeholder="유효 기간 (일)"
                                />
                            </div>
                            <div className={styles.formGroup}>
                                <label>총 수량</label>
                                <input
                                    type="number"
                                    name="totalCount"
                                    value={newCoupon.totalCount}
                                    onChange={handleInputChange}
                                    placeholder="총 수량"
                                />
                            </div>
                            <div className={styles.formGroup}>
                                <label>할인 유형</label>
                                <select
                                    name="discountType"
                                    value={newCoupon.discountType}
                                    onChange={handleInputChange}
                                >
                                    <option value="">선택하세요</option>
                                    {discountTypes.map((type) => (
                                        <option key={type} value={type}>{type}</option>
                                    ))}
                                </select>
                            </div>
                            <div className={styles.formGroup}>
                                <label>최소 주문 금액</label>
                                <input
                                    type="number"
                                    name="minPrice"
                                    value={newCoupon.minPrice}
                                    onChange={handleInputChange}
                                    placeholder="최소 주문 금액"
                                />
                            </div>
                            <div className={styles.formGroup}>
                                <label>할인 금액</label>
                                <input
                                    type="number"
                                    name="discountPrice"
                                    value={newCoupon.discountPrice}
                                    onChange={handleInputChange}
                                    placeholder="할인 금액"
                                />
                            </div>
                            <div className={styles.formGroup}>
                                <label>사용 가능 등급</label>
                                <select
                                    name="eligibleGrade"
                                    value={newCoupon.eligibleGrade}
                                    onChange={handleInputChange}
                                >
                                    <option value="">선택하세요</option>
                                    {eligibleGrades.map((grade) => (
                                        <option key={grade} value={grade}>{grade}</option>
                                    ))}
                                </select>
                            </div>
                            <div className={styles.formGroup}>
                                <label>시작일</label>
                                <input
                                    type="date"
                                    name="startDate"
                                    value={newCoupon.startDate}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div className={styles.formGroup}>
                                <label>만료일</label>
                                <input
                                    type="date"
                                    name="expirationDate"
                                    value={newCoupon.expirationDate}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <button
                                type="button"
                                className={styles.btnAction}
                                onClick={handleCreateCoupon}
                            >
                                생성
                            </button>
                        </form>
                    </div>
                )}
            </div>
        </div>
    );
};

export default CouponManagement;
