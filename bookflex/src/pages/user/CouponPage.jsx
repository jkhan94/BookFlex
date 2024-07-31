import React, { useEffect, useState } from 'react';
import axiosInstance from "../../api/axiosInstance";
import styles from '../user/couponpage.module.css'; // CSS 모듈 임포트

const PAGE_SIZE = 5;

const CouponPage = () => {
    const [myCoupons, setMyCoupons] = useState([]);
    const [availableCoupons, setAvailableCoupons] = useState([]);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);

    useEffect(() => {
        fetchMyCoupons();
        fetchAvailableCoupons();
    }, [currentPage]);

    const fetchMyCoupons = async () => {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axiosInstance.get('/coupons', {
                headers: {
                    Authorization: token,
                    'Content-Type': 'application/json',
                },
                params: {
                    page: currentPage,
                    sortBy: 'isUsed',
                }
            });
            setMyCoupons(response.data);
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
                    page: currentPage,
                    sortBy: 'createdAt',
                }
            });
            setAvailableCoupons(response.data);
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
            // 쿠폰 발급 성공 시, 발급된 쿠폰을 myCoupons 상태에 추가하고 availableCoupons에서 제거
            setMyCoupons(prev => [...prev, response.data]);
            setAvailableCoupons(prev => prev.filter(coupon => coupon.couponId !== couponId));
        } catch (error) {
            setError('쿠폰 발급 중 오류가 발생했습니다: ' + error.message);
        }
    };

    const myCouponIds = new Set(myCoupons.map(coupon => coupon.coupon.couponId));

    return (
        <div>
            <h2>Coupon Page</h2>
            {error && <p className="error">{error}</p>}

            <h3>My Coupons</h3>
            <table className={styles.table}>
                <thead>
                <tr>
                    <th className={styles.th}>쿠폰 코드</th>
                    <th className={styles.th}>발급일</th>
                    <th className={styles.th}>만료일</th>
                    <th className={styles.th}>사용 여부</th>
                    <th className={styles.th}>사용일</th>
                    <th className={styles.th}>쿠폰 이름</th>
                    <th className={styles.th}>쿠폰 유형</th>
                    <th className={styles.th}>할인 타입</th>
                    <th className={styles.th}>최소 가격</th>
                    <th className={styles.th}>할인 가격</th>
                    <th className={styles.th}>적용 등급</th>
                    <th className={styles.th}>상태</th>
                    <th className={styles.th}>발급 시작일</th>
                    <th className={styles.th}>발급 만료일</th>
                </tr>
                </thead>
                <tbody>
                {myCoupons.map(coupon => (
                    <tr key={coupon.userCouponId}>
                        <td className={styles.td}>{coupon.couponCode}</td>
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
                        <td className={styles.td}>{coupon.coupon ? coupon.coupon.couponStatus : '-'}</td>
                        <td className={styles.td}>{coupon.coupon ? coupon.coupon.startDate : '-'}</td>
                        <td className={styles.td}>{coupon.coupon ? coupon.coupon.expirationDate : '-'}</td>
                    </tr>
                ))}
                </tbody>
            </table>

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
                    !myCouponIds.has(coupon.couponId) && (
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
                    )
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default CouponPage;
