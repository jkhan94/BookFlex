import React, { useState, useEffect } from 'react';
import axiosInstance from '../../api/axiosInstance';
import { useNavigate } from 'react-router-dom'; // useNavigate 훅을 임포트
import styles from './CartPage.module.css'; // 스타일을 위한 CSS 모듈 임포트

const CartPage = () => {
    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate(); // useNavigate 훅을 사용하여 리다이렉트

    useEffect(() => {
        const fetchCartItems = async () => {
            try {
                const response = await axiosInstance.get('/baskets', {
                    params: {
                        page: 0,
                        size: 10
                    }
                });
                setCartItems(response.data.data.content); // API 응답에서 장바구니 항목 목록을 추출
            } catch (error) {
                console.error('Error fetching cart items:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchCartItems();
    }, []);

    const handleRemoveItem = async (basketItemId) => {
        try {
            await axiosInstance.delete(`/baskets/${basketItemId}`);
            setCartItems(prevItems => prevItems.filter(item => item.baskeItemid !== basketItemId));
        } catch (error) {
            console.error('Error removing item:', error);
        }
    };

    const handleQuantityChange = async (basketItemId, quantity) => {
        if (quantity < 1) return; // 수량이 1 미만이 되지 않도록 제한

        try {
            await axiosInstance.put(`/baskets/${basketItemId}`, null, {
                params: { quantity }
            });
            setCartItems(prevItems => prevItems.map(item =>
                item.baskeItemid === basketItemId ? { ...item, quantity } : item
            ));
        } catch (error) {
            console.error('Error updating quantity:', error);
        }
    };

    const calculateTotal = () => {
        return cartItems.reduce((total, item) => total + item.price * item.quantity, 0);
    };

    const handleCheckout = async () => {
        try {
            const orderRequestDto = {
                items: cartItems.map(item => ({
                    bookId: item.bookId,
                    price: item.price,
                    quantity: item.quantity
                }))
            };

            const response = await axiosInstance.post('/orders', orderRequestDto);
            const orderId = response.data.orderId; // 응답에서 주문 ID를 추출

            // 주문 페이지로 리다이렉트
            navigate(`/main/order/${orderId}`);
        } catch (error) {
            console.error('Error creating order:', error);

            // 에러 응답에서 메시지를 추출하여 알림 창을 띄움
            if (error.response && error.response.data && error.response.data.message) {
                alert(error.response.data.message);
            } else {
                alert('An error occurred while creating the order.');
            }
        }
    };

    if (loading) return <div>Loading...</div>;

    return (
        <div className={styles.cartPage}>
            <div className={styles.cartCard}>
                <h1 className={styles.title}>나의 예쁜 장바구니</h1>
                <div className={styles.cartItems}>
                    {cartItems.length === 0 ? (
                        <p>No items in the cart.</p>
                    ) : (
                        cartItems.map(item => (
                            <div key={item.baskeItemid} className={`${styles.cartItem} ${item.removing ? styles.cartItemRemoving : ''}`}>
                                <img src={item.photoImagePath} alt={item.bookName} className={styles.itemImage} />
                                <div className={styles.itemDetails}>
                                    <div className={styles.itemTitle}>{item.bookName}</div>
                                    <div className={styles.itemPrice}>₩{item.price.toLocaleString()}</div>
                                </div>
                                <div className={styles.itemActions}>
                                    <button
                                        className={styles.quantityBtn}
                                        onClick={() => handleQuantityChange(item.baskeItemid, item.quantity - 1)}
                                        disabled={item.quantity <= 1}
                                    >
                                        -
                                    </button>
                                    <input
                                        type="number"
                                        className={styles.quantityInput}
                                        value={item.quantity}
                                        min="1"
                                        onChange={(e) => handleQuantityChange(item.baskeItemid, parseInt(e.target.value))}
                                    />
                                    <button
                                        className={styles.quantityBtn}
                                        onClick={() => handleQuantityChange(item.baskeItemid, item.quantity + 1)}
                                    >
                                        +
                                    </button>
                                    <button
                                        className={styles.removeBtn}
                                        onClick={() => handleRemoveItem(item.baskeItemid)}
                                    >
                                        ×
                                    </button>
                                </div>
                            </div>
                        ))
                    )}
                </div>
                <div className={styles.cartTotal}>
                    <span>총 금액:</span>
                    <span>₩{calculateTotal().toLocaleString()}</span>
                </div>
                <div className={styles.checkoutContainer}>
                    <button className={styles.checkoutBtn} onClick={handleCheckout}>주문하기</button>
                </div>
            </div>
        </div>
    );
};

export default CartPage;
