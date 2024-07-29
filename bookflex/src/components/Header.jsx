import React from 'react';
import { Link } from 'react-router-dom';
import './Header.css'; // 스타일 시트 임포트

const Header = ({  }) => {
    const handleLogout = () => {
        // 로그아웃 로직을 여기에 추가합니다.
        localStorage.removeItem('Authorization'); // 예시: 토큰 삭제
        window.location.href = '/login'; // 로그인 페이지로 리디렉션
    };
    return (
        <header className="header">
            <nav>
                <ul className="navList">
                    <li><Link to="/main/dashboard">Main</Link></li>
                    <li><Link to="/main/category">Categories</Link></li>
                    <li><Link to="/main/cart">Cart</Link></li>
                    <li><Link to="/main/order">Order</Link></li>
                    <li><Link to="/main/payment-history">Payment History</Link></li>
                    <li><Link to="/main/wishlist">Wishlist</Link></li>
                    <li><Link to="/profile">Profile</Link></li>
                    <li>
                        <button className="logoutButton" onClick={handleLogout}>Logout</button>
                    </li>
                </ul>
            </nav>
        </header>
    );
};

export default Header;
