import React, {useState, useEffect} from 'react';
import {BrowserRouter as Router, Routes, Route, Navigate} from 'react-router-dom';
import Header from './components/Header'; // 경로 확인
import Sidebar from './components/Sidebar'; // 경로 확인
import UserMainPage from './pages/user/UserMainPage';
import CategoryPage from './pages/user/CategoryPage';
import CartPage from './pages/user/CartPage';
import OrderPage from './pages/user/OrderPage';
import PaymentHistoryPage from './pages/user/PaymentHistoryPage';
import WishlistPage from './pages/user/WishlistPage';
import AdminDashboard from './pages/admin/AdminDashboard';
import OrderManagement from './pages/admin/OrderManagement';
import ProductManagement from './pages/admin/ProductManagement';
import CouponManagement from './pages/admin/CouponManagement';
import SalesReport from './pages/admin/SalesReport';
import PaymentManagement from './pages/admin/PaymentManagement';
import ShippingManagement from './pages/admin/ShippingManagement';
import LoginPage from './pages/common/LoginPage';
import SignUpPage from './pages/common/SignUpPage'; // 수정된 이름
import UserLayout from './pages/user/UserLayout'; // UserLayout 컴포넌트 임포트
import ProfilePage from './pages/user/ProfilePage';
import ProfileModifyPage from "./pages/user/ProfileModifyPage";
import UserQnAPage from './pages/user/UserQnAPage'; // 유저 Q&A 페이지 임포트
import AdminQnAPage from './pages/admin/AdminQnAPage'
import BookDetailPage from './pages/user/BookDetailPage';
import BookDetailPages from './pages/admin/BookDetailPages';
import RegisterBookPage from "./pages/admin/RegisterBookPage";
import BookListPage from "./pages/admin/BookListPage";
import ModifyBookDetail from "./pages/admin/BookUpdatePage";
import BookUpdatePage from "./pages/admin/BookUpdatePage";
import CheckoutPage from "./pages/payment/Checkout";
import FailPage from "./pages/payment/Fail";
import SuccessPage from "./pages/payment/Success";
import axiosInstance from './api/axiosInstance';
import CouponPage from "./pages/user/CouponPage";
import ReviewListPage from "./pages/admin/ReviewListPage";
import RegisterReviewPage from "./pages/admin/RegisterReviewPage";
import ReviewDetailPage from "./pages/admin/ReviewDetailPage";
import SaleReportByBookNamePage from "./pages/admin/SaleReportByBookNamePage";
import SaleReportByCategoryNamePage from "./pages/admin/SaleReportByCategoryNamePage";
import MemberListPage from "./pages/admin/MemberListPage";


function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [userRole, setUserRole] = useState(null);

    useEffect(() => {
        const accessToken = localStorage.getItem('Authorization');
        if (accessToken) {
            axiosInstance.defaults.headers.common['Authorization'] = accessToken;
        }
    }, []);
    const handleLogin = (role) => {
        setIsLoggedIn(true);
        setUserRole(role);
    };

    const handleLogout = () => {
        setIsLoggedIn(false);
        setUserRole(null);
        localStorage.removeItem('Authorization'); // 토큰 삭제
    };

    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route
                        path="/"
                        element={isLoggedIn ? (
                            userRole === 'admin' ? <Navigate to="/admin"/> : <Navigate to="/main"/>
                        ) : (
                            <LoginPage onLogin={handleLogin}/>
                        )}
                    />
                    <Route path="/login" element={<LoginPage onLogin={handleLogin}/>}/>
                    <Route path="/signup" element={<SignUpPage/>}/>
                    <Route path="/success" element={<SuccessPage/>}/>
                    <Route path="/fail" element={<FailPage/>}/>
                    <Route path="/checkout" element={<CheckoutPage/>}/>
                    <Route path="/books/:bookId" element={<BookDetailPage/>}/>

                    <Route path="/main" element={<UserLayout/>}>
                        <Route path="dashboard" element={<UserMainPage/>}/>
                        <Route path="cart" element={<CartPage/>}/>
                        <Route path="order/:orderId" element={<OrderPage/>}/>
                        <Route path="coupon" element={<CouponPage/>}/>
                        <Route path="payment-history" element={<PaymentHistoryPage/>}/>
                        <Route path="category/:categoryName" element={<CategoryPage/>}/>
                        <Route path="wishlist" element={<WishlistPage/>}/>
                        <Route path="profile" element={<ProfilePage/>}/>
                        <Route path="qna" element={<UserQnAPage/>}/>
                        {/* 추가적인 유저 하위 라우트 설정 */}
                        <Route path="proZZfile-modify" element={<ProfileModifyPage/>}/>
                    </Route>


                    <Route path="/admin" element={<AdminDashboard/>}>
                        <Route path="RegisterBook" element={<RegisterBookPage/>}/>
                        <Route path="products" element={<ProductManagement/>}/>
                        <Route path="coupons" element={<CouponManagement/>}/>
                        <Route path="sales" element={<SalesReport/>}/>
                        <Route path="payments" element={<PaymentManagement/>}/>
                        <Route path="shipping" element={<ShippingManagement/>}/>
                        <Route path="qna" element={<AdminQnAPage/>}/>
                        <Route path="inquiry-book" element={<BookDetailPages/>}/>
                        <Route path="register-book" element={<RegisterBookPage/>}/>
                        <Route path="inquiry-booklist" element={<BookListPage/>}/>
                        <Route path="modify-book-info" element={<BookUpdatePage/>}/>
                        <Route path="books/:bookId" element={<BookDetailPages/>}/>
                        <Route path="books/:productId/edit" element={<BookUpdatePage/>}/>
                        <Route path="" element={<BookListPage/>}/>
                        <Route path="register-book" element={<RegisterBookPage/>}/>
                        <Route path="Review-List" element={<ReviewListPage/>}/>
                        <Route path="register-review" element={<RegisterReviewPage/>}/>
                        <Route path="inquiry-review" element={<ReviewDetailPage/>}/>
                        <Route path="salereport-bybookname" element={<SaleReportByBookNamePage/>}/>
                        <Route path="reviews/:reviewId" element={<ReviewDetailPage/>}/>
                        <Route path="salereport-bycategory" element={<SaleReportByCategoryNamePage/>}/>
                        <Route path="member-list" element={<MemberListPage/>}/>

                        {/* 추가적인 관리자 하위 라우트 설정 */}
                    </Route>
                </Routes>
            </div>
        </Router>
    );
}

export default App;