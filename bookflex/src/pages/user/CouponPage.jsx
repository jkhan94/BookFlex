import React, {useEffect, useState} from 'react';
import axiosInstance from "../../api/axiosInstance";
import styles from '../user/couponpage.module.css'; // CSS 모듈 임포트

const CouponPage = () => {
    const [myCoupons, setMyCoupons] = useState([]);
    const [availableCoupons, setAvailableCoupons] = useState([]);
    const [error, setError] = useState(null);

    const [currentPageMyCoupons, setCurrentPageMyCoupons] = useState(1); // My Coupons 페이지
    const [currentPageAvailableCoupons, setCurrentPageAvailableCoupons] = useState(1); // Available Coupons 페이지

    const [totalPagesMyCoupons, setTotalPagesMyCoupons] = useState(1);
    const [totalPagesAvailableCoupons, setTotalPagesAvailableCoupons] = useState(1);

    useEffect(() => {
        fetchMyCoupons();
    }, [currentPageMyCoupons]);

    useEffect(() => {
        fetchAvailableCoupons();
    }, [currentPageAvailableCoupons]);

    const fetchMyCoupons = async () => {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axiosInstance.get('/coupons', {
                headers: {
                    Authorization: token,
                    'Content-Type': 'application/json',
                },
                params: {
                    page: currentPageMyCoupons, // My Coupons 페이지 번호 (1 기반)
                    sortBy: 'isUsed',
                }
            });
            setMyCoupons(response.data.content);
            setTotalPagesMyCoupons(response.data.totalPages);
        } catch (error) {
            setError('내 쿠폰을 불러오는 중 오류가 발생했습니다: ' + error.message);
        }
    };

    const fetchAvailableCoupons = async () => {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axiosInstance.get('/coupons/availables', {
                headers: {
                    Authorization: token,
                    'Content-Type': 'application/json',
                },
                params: {
                    page: currentPageAvailableCoupons, // Available Coupons 페이지 번호 (1 기반)
                    sortBy: 'createdAt',
                }
            });
            setAvailableCoupons(response.data.content);
            setTotalPagesAvailableCoupons(response.data.totalPages);
        } catch (error) {
            setError('사용 가능한 쿠폰을 불러오는 중 오류가 발생했습니다: ' + error.message);
        }
    };

    const issueCoupon = async (couponId) => {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axiosInstance.post(`/coupons/issue/${couponId}`, {}, {
                headers: {
                    Authorization: token,
                    'Content-Type': 'application/json',
                }
            });
            // 쿠폰 발급 성공 시, 쿠폰 목록을 새로고침
            fetchMyCoupons();
            fetchAvailableCoupons();
        } catch (error) {
            setError('쿠폰 발급 중 오류가 발생했습니다: ' + error.message);
        }
    };

    const handlePreviousPageMyCoupons = () => {
        if (currentPageMyCoupons > 1) {
            setCurrentPageMyCoupons(currentPageMyCoupons - 1);
        }
    };

    const handleNextPageMyCoupons = () => {
        if (currentPageMyCoupons < totalPagesMyCoupons) {
            setCurrentPageMyCoupons(currentPageMyCoupons + 1);
        }
    };

    const handlePreviousPageAvailableCoupons = () => {
        if (currentPageAvailableCoupons > 1) {
            setCurrentPageAvailableCoupons(currentPageAvailableCoupons - 1);
        }
    };

    const handleNextPageAvailableCoupons = () => {
        if (currentPageAvailableCoupons < totalPagesAvailableCoupons) {
            setCurrentPageAvailableCoupons(currentPageAvailableCoupons + 1);
        }
    };

    return (
        <div>
            <h2>Coupon Page</h2>
            {error && <p className="error">{error}</p>}

            <h3>My Coupons</h3>
            <table className={styles.table}>
                <thead>
                <tr>
                    {/*<th className={styles.th}>쿠폰 코드</th>*/}
                    <th className={styles.th}>쿠폰 발급일</th>
                    <th className={styles.th}>사용 만료일</th>
                    <th className={styles.th}>사용 여부</th>
                    <th className={styles.th}>쿠폰 사용일</th>
                    <th className={styles.th}>쿠폰 이름</th>
                    <th className={styles.th}>쿠폰 유형</th>
                    <th className={styles.th}>할인 타입</th>
                    <th className={styles.th}>최소 가격</th>
                    <th className={styles.th}>할인 가격</th>
                    <th className={styles.th}>적용 등급</th>
                </tr>
                </thead>
                <tbody>
                {myCoupons.map(coupon => (
                    <tr key={coupon.userCouponId}>
                        {/*<td className={styles.td}>{coupon.couponCode}</td>*/}
                        <td className={styles.td}>{coupon.issuedAt}</td>
                        <td className={styles.td}>{coupon.expirationDate}</td>
                        <td className={styles.td}>{coupon.isUsed ? '사용됨' : '미사용'}</td>
                        <td className={styles.td}>{coupon.usedAt ? coupon.usedAt : '-'}</td>
                        <td className={styles.td}>{coupon.coupon ? coupon.coupon.couponName : '-'}</td>
                        <td className={styles.td}>{coupon.coupon ? coupon.coupon.couponType : '-'}</td>
                        <td className={styles.td}>{coupon.coupon ? coupon.coupon.discountType : '-'}</td>
                        <td className={styles.td}>{coupon.coupon ? coupon.coupon.minPrice : '-'}</td>
                        <td className={styles.td}>{coupon.coupon ? coupon.coupon.discountPrice : '-'}</td>
                        <td className={styles.td}>{coupon.coupon ? coupon.coupon.eligibleGrade : '-'}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            <div className={styles.pagination}>
                <button onClick={handlePreviousPageMyCoupons} disabled={currentPageMyCoupons === 1}>Previous</button>
                <span>페이지 {currentPageMyCoupons} / {totalPagesMyCoupons}</span>
                <button onClick={handleNextPageMyCoupons} disabled={currentPageMyCoupons >= totalPagesMyCoupons}>Next
                </button>
            </div>

            <h3>Available Coupons</h3>
            <table className={styles.table}>
                <thead>
                <tr>
                    <th className={styles.th}>쿠폰 이름</th>
                    <th className={styles.th}>쿠폰 유형</th>
                    <th className={styles.th}>할인 타입</th>
                    <th className={styles.th}>최소 가격</th>
                    <th className={styles.th}>할인 가격</th>
                    <th className={styles.th}>적용 등급</th>
                    <th className={styles.th}>상태</th>
                    <th className={styles.th}>발급 시작일</th>
                    <th className={styles.th}>발급 만료일</th>
                    <th className={styles.th}>작업</th>
                </tr>
                </thead>
                <tbody>
                {availableCoupons.map(coupon => (
                    <tr key={coupon.couponId}>
                        <td className={styles.td}>{coupon.couponName}</td>
                        <td className={styles.td}>{coupon.couponType}</td>
                        <td className={styles.td}>{coupon.discountType}</td>
                        <td className={styles.td}>{coupon.minPrice}</td>
                        <td className={styles.td}>{coupon.discountPrice}</td>
                        <td className={styles.td}>{coupon.eligibleGrade}</td>
                        <td className={styles.td}>{coupon.couponStatus}</td>
                        <td className={styles.td}>{coupon.startDate}</td>
                        <td className={styles.td}>{coupon.expirationDate}</td>
                        <td className={styles.td}>
                            <button
                                className={styles.issueButton}
                                onClick={() => issueCoupon(coupon.couponId)}
                            >
                                쿠폰 발급
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            <div className={styles.pagination}>
                <button onClick={handlePreviousPageAvailableCoupons}
                        disabled={currentPageAvailableCoupons === 1}>Previous
                </button>
                <span>페이지 {currentPageAvailableCoupons} / {totalPagesAvailableCoupons}</span>
                <button onClick={handleNextPageAvailableCoupons}
                        disabled={currentPageAvailableCoupons >= totalPagesAvailableCoupons}>Next
                </button>
            </div>
        </div>
    );
};

export default CouponPage;
