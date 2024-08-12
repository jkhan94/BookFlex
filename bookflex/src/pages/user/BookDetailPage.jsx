// BookDetailPage.jsx
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance'; // API 호출을 위한 인스턴스
import styles from './BookDetailPage.module.css'; // 스타일을 위한 CSS 모듈 임포트

const BookDetailPage = () => {
    const { bookId } = useParams(); // URL에서 책 ID 가져오기
    const [book, setBook] = useState(null);
    const [quantity, setQuantity] = useState(1);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchBookDetails = async () => {
            try {
                const response = await axiosInstance.get(`/books/${bookId}`);
                setBook(response.data.data); // 응답 데이터 확인 후 수정 필요
            } catch (error) {
                console.error("Error fetching book details", error);
            }
        };
        fetchBookDetails();
    }, [bookId]);

    const handleAddToCart = async () => {
        try {
            const response = await axiosInstance.post('/baskets', {
                bookId,
                quantity
            });
            const shouldNavigate = window.confirm("장바구니로 이동하시겠습니까?");
            if (shouldNavigate) {
                navigate('/main/cart'); // 장바구니 페이지로 이동
            }// 페이지 이동
        } catch (error) {
            console.log("장바구니 담기 실패", error);
        }
    };

    const handleAddToWishlist = async () => {
        try {
            await axiosInstance.post(`/wishs/${bookId}`);
            const shouldNavigate = window.confirm("위시리스트로 이동하시겠습니까?");
            if (shouldNavigate) {
                navigate('/main/wishlist'); // 위시리스트 페이지로 이동
            }
        } catch (error) {
            console.log("위시리스트 담기 실패", error);
        }
    };

    const handleQuantityChange = (e) => {
        setQuantity(Number(e.target.value));
    };

    if (!book) {
        return <div>Loading...</div>; // 로딩 중 표시
    }

    return (
        <div className={styles.bookDetailPage}>
            <h1>{book.bookName}</h1>
            <div className={styles.bookDetails}>
                <img src={book.photoImagePath} alt={book.bookName} className={styles.bookImage} />
                <div className={styles.bookInfo}>
                    <h2>{book.bookName}</h2>
                    <p><strong>Author:</strong> {book.author}</p>
                    <p><strong>Publisher:</strong> {book.publisher}</p>
                    <p><strong>Main Category:</strong> {book.mainCategoryName}</p>
                    <p><strong>Sub Category:</strong> {book.subCategoryName}</p>
                    <p><strong>Price:</strong> ${book.price}</p>
                    <p><strong>Status:</strong> {book.status}</p>
                    <p><strong>Stock:</strong> {book.stock}</p>
                    <p><strong>Description:</strong> {book.bookDescription}</p>
                    <p><strong>Created At:</strong> {new Date(book.createdAt).toLocaleString()}</p>
                    <p><strong>Modified At:</strong> {new Date(book.modifiedAt).toLocaleString()}</p>
                    <div className={styles.quantityContainer}>
                        <label htmlFor="quantity">Quantity:</label>
                        <input
                            type="number"
                            id="quantity"
                            value={quantity}
                            min="1"
                            max={book.stock}
                            onChange={handleQuantityChange}
                            className={styles.quantityInput}
                        />
                    </div>
                    <button onClick={handleAddToCart} className={styles.addToCartButton}>Add to Cart</button>
                    <button onClick={handleAddToWishlist} className={styles.addToWishlistButton}>Add to Wishlist
                    </button>
                </div>
            </div>
        </div>
    );
};

export default BookDetailPage;
