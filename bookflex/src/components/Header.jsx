import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axiosInstance from "../api/axiosInstance";
import styles from './Header.module.css'; // CSS 모듈 import

const Header = () => {
    const [categories, setCategories] = useState([]);
    const [showCategories, setShowCategories] = useState(false);
    const [showSubCategories, setShowSubCategories] = useState({});
    const [selectedCategory, setSelectedCategory] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const response = await axiosInstance.get('/categories');
                if (response.data && Array.isArray(response.data)) {
                    setCategories(response.data);
                } else {
                    console.error('Invalid response structure:', response);
                }
            } catch (error) {
                console.error('Error fetching categories:', error.response?.data || error);
            } finally {
                setLoading(false);
            }
        };

        fetchCategories();
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('Authorization');
        window.location.href = '/login';
    };

    const toggleCategories = () => {
        setShowCategories(!showCategories);
    };

    const toggleSubCategories = (categoryName) => {
        setShowSubCategories((prev) => ({
            ...prev,
            [categoryName]: !prev[categoryName]
        }));
    };

    const handleCategoryClick = (mainCategoryName) => {
        setSelectedCategory(mainCategoryName);
        // 카테고리 페이지로 리디렉션
        window.location.href = `/categories/${mainCategoryName}`;
    };

    return (
        <header className={styles.header}>
            <nav className={styles.nav}>
                <ul className={styles.navList}>
                    <li><Link to="/main/dashboard">Main</Link></li>
                    <li
                        className={styles.categoryItem}
                        onMouseEnter={toggleCategories}
                        onMouseLeave={toggleCategories}
                    >
                        Categories
                        {showCategories && (
                            <ul className={styles.dropdown}>
                                {categories.length > 0 ? (
                                    categories.map((category) => (
                                        <li
                                            key={category.mainCategoryName}
                                            className={styles.dropdownItem}
                                            onMouseEnter={() => toggleSubCategories(category.mainCategoryName)}
                                            onMouseLeave={() => toggleSubCategories(category.mainCategoryName)}
                                        >
                                            {category.mainCategoryName}
                                            {showSubCategories[category.mainCategoryName] && (
                                                <ul className={styles.subDropdown}>
                                                    {category.subCategoryNames.map((subCategory) => {
                                                        // URL 인코딩 처리
                                                        const encodedSubCategory = encodeURIComponent(subCategory.toUpperCase());

                                                        return (
                                                            <li key={encodedSubCategory} className={styles.dropdownItem}>
                                                                <Link to={`/main/category/${encodedSubCategory}`}>
                                                                    {subCategory}
                                                                </Link>
                                                            </li>
                                                        );
                                                    })}
                                                </ul>
                                            )}
                                        </li>
                                    ))
                                ) : (
                                    <li className={styles.dropdownItem}>No categories available</li>
                                )}
                            </ul>
                        )}
                    </li>
                    <li><Link to="/main/cart">Cart</Link></li>
                    <li><Link to="/main/order">Order</Link></li>
                    <li><Link to="/main/payment-history">Payment History</Link></li>
                    <li><Link to="/main/wishlist">Wishlist</Link></li>
                    <li><Link to="/main/profile">Profile</Link></li>
                    <li><Link to="/main/qna">User Q&A</Link></li>
                    <li>
                        <button className={styles.logoutButton} onClick={handleLogout}>Logout</button>
                    </li>
                </ul>
            </nav>
        </header>
    );
};

export default Header;
