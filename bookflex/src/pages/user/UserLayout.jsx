import React from 'react';
import { Outlet } from 'react-router-dom';
import Header from '../../components/Header'; // 헤더 컴포넌트 임포트
import styles from './UserLayout.module.css'; // 스타일 모듈

const UserLayout = () => {
    return (
        <div className={styles.container}>
            <Header />
            <div className={styles.content}>
                <Outlet /> {/* 하위 페이지 내용 */}
            </div>
        </div>
    );
};

export default UserLayout;