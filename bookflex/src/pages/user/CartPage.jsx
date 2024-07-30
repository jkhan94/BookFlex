import React, { useState, useEffect } from 'react';
import axiosInstance from '../../api/axiosInstance';
import './CartPage.css'; // 스타일을 위한 CSS 파일 임포트

const CartPage = () => {
    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);

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

    if (loading) return <div>Loading...</div>;

    return (
        <div className="cart-page">
            <div className="cart-card">
                <h1>나의 예쁜 장바구니</h1>
                <div className="cart-items">
                    {cartItems.length === 0 ? (
                        <p>No items in the cart.</p>
                    ) : (
                        cartItems.map(item => (
                            <div key={item.baskeItemid} className={`cart-item ${item.removing ? 'removing' : ''}`}>
                                <img src={item.photoImagePath} alt={item.bookName} className="item-image" />
                                <div className="item-details">
                                    <div className="item-title">{item.bookName}</div>
                                    <div className="item-price">₩{item.price.toLocaleString()}</div>
                                </div>
                                <div className="item-actions">
                                    <button
                                        className="quantity-btn"
                                        onClick={() => handleQuantityChange(item.baskeItemid, item.quantity - 1)}
                                        disabled={item.quantity <= 1}
                                    >
                                        -
                                    </button>
                                    <input
                                        type="number"
                                        className="quantity-input"
                                        value={item.quantity}
                                        min="1"
                                        onChange={(e) => handleQuantityChange(item.baskeItemid, parseInt(e.target.value))}
                                    />
                                    <button
                                        className="quantity-btn"
                                        onClick={() => handleQuantityChange(item.baskeItemid, item.quantity + 1)}
                                    >
                                        +
                                    </button>
                                    <button
                                        className="remove-btn"
                                        onClick={() => handleRemoveItem(item.baskeItemid)}
                                    >
                                        ×
                                    </button>
                                </div>
                            </div>
                        ))
                    )}
                </div>
                <div className="cart-total">
                    <span>총 금액:</span>
                    <span>₩{calculateTotal().toLocaleString()}</span>
                </div>
                <div className="checkout-container">
                    <button className="checkout-btn" onClick={() => alert('주문이 완료되었습니다. 감사합니다!')}>주문하기</button>
                </div>
            </div>
        </div>
    );
};

export default CartPage;
