import React from 'react';
import { Link, Outlet } from 'react-router-dom';
import styles from './AdminDashboard.module.css';

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
                <h2 className={styles.sidebarTitle}>Admin Menu</h2>
                <ul className={styles.navList}>
                    <li><Link to="/admin/orders">Order Management</Link></li>
                    <li><Link to="/admin/products">Product Management</Link></li>
                    <li><Link to="/admin/coupons">Coupon Management</Link></li>
                    <li><Link to="/admin/sales">Sales Report</Link></li>
                    <li><Link to="/admin/payments">Payment Management</Link></li>
                    <li><Link to="/admin/shipping">Shipping Management</Link></li>
                </ul>
                <button className={styles.logoutButton} onClick={handleLogout}>Logout</button>
            </aside>
            <main className={styles.mainContent}>
                <h1>Welcome to Admin Dashboard</h1>
                <Outlet />
            </main>
        </div>
    );
};

export default AdminDashboard;
