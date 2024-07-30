import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance';
import './BookDetailPages.css';

const BookDetailPage = () => {
    const { bookId } = useParams();
    const navigate = useNavigate();
    const [book, setBook] = useState(null);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchBook = async () => {
            try {
                const response = await axiosInstance.get(`/books/${bookId}`);
                setBook(response.data.data);
            } catch (error) {
                console.error('There was an error!', error);
                setError('상품 조회에 실패하였습니다.');
            }
        };

        fetchBook();
    }, [bookId]);

    if (error) {
        return <p>{error}</p>;
    }

    if (!book) {
        return <p>Loading...</p>;
    }

    const handleEditClick = () => {
        navigate(`/admin/books/${bookId}/edit`, { state: { book } });
    };

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
                        <label>출판사:</label>
                        <span>{book.publisher}</span>
                    </div>
                    <div>
                        <label>작가:</label>
                        <span>{book.author}</span>
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
                        <label>재고:</label>
                        <span>{book.stock}권</span>
                    </div>
                    <div>
                        <label>상품 설명:</label>
                        <p>{book.bookDescription}</p>
                    </div>
                    <div>
                        <label>상품 상태:</label>
                        <p>{book.status}</p>
                    </div>
                    <div>
                        <label>등록 일자:</label>
                        <p>{book.createdAt}</p>
                    </div>
                    <button onClick={handleEditClick}>수정</button>
                </div>
            </div>
        </div>
    );
};

export default BookDetailPage;
