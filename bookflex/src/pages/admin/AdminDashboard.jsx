import React from 'react';
import {Link, Outlet} from 'react-router-dom';
import styles from './AdminDashboard.module.css';
// 경로에 맞게 임포트

const AdminDashboard = () => {
    // 로그아웃 핸들러 함수
    const handleLogout = () => {
        // 로그아웃 로직을 여기에 추가합니다.
        localStorage.removeItem('Authorization'); // 예시: 토큰 삭제
        window.location.href = '/login'; // 로그인 페이지로 리디렉션
    };

    return (
        <div className={styles.dashboardContainer}>
            <aside className={styles.sidebar}>
                <h2 className={styles.sidebarTitle}>관리자 메뉴</h2>
                <ul className={styles.navList}>
                    <li><Link to="/admin/register-book">상품 등록</Link></li>
                    <li><Link to="/admin/inquiry-booklist">상품 관리</Link></li>
                    <li><Link to="/admin/coupons">매출 내역</Link></li>
                    <li><Link to="/admin/sales">Sales Report</Link></li>
                    <li><Link to="/admin/payments">Payment Management</Link></li>
                    <li><Link to="/admin/shipping">Shipping Management</Link></li>
                    <li><Link to="/admin/qna">Admin Q&A</Link></li>
                    {/* 새로운 링크 추가 */}
                </ul>
                <button className={styles.logoutButton} onClick={handleLogout}>Logout</button>
            </aside>
            <main className={styles.mainContent}>
                <h1>관리자 페이지</h1>
                <Outlet/>
            </main>
        </div>
    );
};

export default AdminDashboard;
