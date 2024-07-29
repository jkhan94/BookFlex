import React from 'react';
import { Link } from 'react-router-dom';
import './Sidebar.css';

const Sidebar = () => {
    return (
        <aside className="sidebar">
            <nav>
                <ul>
                    <li><Link to="/">Dashboard</Link></li>
                    <li><Link to="/admin/orders">Order Management</Link></li>
                    <li><Link to="/admin/products">Product Management</Link></li>
                    <li><Link to="/admin/coupons">Coupon Management</Link></li>
                    <li><Link to="/admin/sales">Sales Report</Link></li>
                    <li><Link to="/admin/payments">Payment Management</Link></li>
                    <li><Link to="/admin/shipping">Shipping Management</Link></li>
                    <li><Link to="/admin/qna">Admin Q&A</Link></li>
                </ul>
            </nav>
        </aside>
    );
};

export default Sidebar;