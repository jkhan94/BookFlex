import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import './BookDetailPage.css';
import axiosInstance from "../../api/axiosInstance"; // 필요한 CSS 파일을 가져옵니다.

const BookDetailPage = () => {
    const { productId } = useParams();
    const [book, setBook] = useState(null);
    const [error, setError] = useState('');
    const [accessToken, setAccessToken] = useState(''); // 액세스 토큰 상태 추가



    useEffect(() => {
        const fetchBook = async () => {
            try {
                const token = localStorage.getItem('Authorization');
                setAccessToken(token);
                const response = await axiosInstance.get('/books/1', {
                    headers: {
                        Authorization: accessToken, // 액세스 토큰을 Authorization 헤더에 추가합니다
                    },
                });
                setBook(response.data.data);
            } catch (error) {
                console.error('There was an error!', error);
                setError('상품 조회에 실패하였습니다.');
            }
        };

        fetchBook();
    }, [productId]);

    if (error) {
        return <p>{error}</p>;
    }

    if (!book) {
        return <p>Loading...</p>;
    }

    return (
        <div className="book-detail-container">
            <h1>상품 상세</h1>
            <div className="book-detail">
                <div className="book-image">
                    <img src={book.photoImagePath} alt={book.bookName} />
                </div>
                <div className="book-info">
                    <div>
                        <label>상품명:</label>
                        <span>{book.bookName}</span>
                    </div>
                    <div>
                        <label>카테고리:</label>
                        <span>{book.mainCategoryName} / {book.subCategoryName}</span>
                    </div>
                    <div>
                        <label>상품가격:</label>
                        <span>{book.price.toLocaleString()}원</span>
                    </div>
                    <div>
                        <label>할인여부(%):</label>
                        <span>{book.discountRate}%</span>
                    </div>
                    <div>
                        <label>재고:</label>
                        <span>{book.stock}</span>
                    </div>
                    <div>
                        <label>출판사:</label>
                        <span>{book.publisher}</span>
                    </div>
                    <div>
                        <label>작가:</label>
                        <span>{book.author}</span>
                    </div>
                    <div>
                        <label>상품 설명:</label>
                        <p>{book.bookDescription}</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default BookDetailPage;
