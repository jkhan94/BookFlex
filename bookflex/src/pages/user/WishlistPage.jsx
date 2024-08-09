import React, { useState, useEffect } from 'react';
import axiosInstance from '../../api/axiosInstance'; // Axios 인스턴스 경로에 맞게 수정
import { useNavigate } from 'react-router-dom'; // useNavigate를 사용하여 페이지 이동
import styles from './WishlistPage.module.css';

const WishlistPage = () => {
    const [wishlistItems, setWishlistItems] = useState([]);
    const [selectedItems, setSelectedItems] = useState([]);
    const navigate = useNavigate(); // 페이지 이동을 위한 hook

    // 데이터 로드
    useEffect(() => {
        const fetchWishlist = async () => {
            try {
                const response = await axiosInstance.get('/wishs');
                const data = response.data.content.map(item => ({
                    wishId: item.wishId, // wishId는 삭제에 필요
                    bookId: item.bookId,
                    name: item.bookName,
                    image: item.photoUrl
                }));
                setWishlistItems(data);
            } catch (error) {
                console.error('위시리스트를 가져오는 데 실패했습니다.', error);
            }
        };
        fetchWishlist();
    }, []);

    const handleCheckboxChange = (bookId) => {
        setSelectedItems(prevSelectedItems =>
            prevSelectedItems.includes(bookId)
                ? prevSelectedItems.filter(id => id !== bookId)
                : [...prevSelectedItems, bookId]
        );
    };

    const handleAddToCart = async () => {
        if (selectedItems.length > 0) {
            try {
                // 선택한 아이템을 장바구니에 추가하기 위한 요청 데이터 생성
                const basketRequests = selectedItems.map(bookId => ({
                    bookId: bookId,
                    quantity: 1 // 기본 수량은 1로 설정
                }));

                await Promise.all(
                    basketRequests.map(item =>
                        axiosInstance.post('/baskets', item)
                    )
                );

                // 장바구니에 추가 후 위시리스트에서 선택한 아이템 삭제
                await Promise.all(
                    selectedItems.map(bookId =>
                        axiosInstance.delete(`/wishs/${wishlistItems.find(item => item.bookId === bookId).wishId}`)
                    )
                );

                // 위시리스트 상태 업데이트
                setWishlistItems(prevItems => prevItems.filter(item => !selectedItems.includes(item.bookId)));

                alert('선택한 상품이 장바구니에 담겼습니다.');
                navigate('/main/cart'); // 장바구니 페이지로 이동
            } catch (error) {
                console.error('장바구니에 아이템을 추가하는 데 실패했습니다.', error);
                alert('장바구니에 아이템을 추가하는 데 실패했습니다.');
            }
        } else {
            alert('선택한 상품이 없습니다. 원하는 상품을 체크해주세요.');
        }
    };

    const handleDelete = async (wishId) => {
        try {
            await axiosInstance.delete(`/wishs/${wishId}`);
            setWishlistItems(prevItems => prevItems.filter(item => item.wishId !== wishId));
            alert('위시리스트에서 아이템이 삭제되었습니다.');
        } catch (error) {
            console.error('위시리스트에서 아이템을 삭제하는 데 실패했습니다.', error);
        }
    };

    return (
        <div className={styles.container}>
            <h1>나의 위시리스트</h1>
            <ul className={styles.wishlist}>
                {wishlistItems.map(item => (
                    <li key={item.bookId} className={styles.wishlistItem}>
                        <input
                            type="checkbox"
                            className={styles.itemCheckbox}
                            checked={selectedItems.includes(item.bookId)}
                            onChange={() => handleCheckboxChange(item.bookId)}
                        />
                        <img src={item.image} alt={item.name} className={styles.itemImage} />
                        <div className={styles.itemDetails}>
                            <div className={styles.itemName}>{item.name}</div>
                        </div>
                        <button
                            className={styles.deleteBtn}
                            onClick={() => handleDelete(item.wishId)}
                        >
                            삭제
                        </button>
                    </li>
                ))}
            </ul>
            <button className={styles.addToCartBtn} onClick={handleAddToCart}>
                선택한 상품 장바구니 담기
            </button>
        </div>
    );
};

export default WishlistPage;
