// src/pages/user/BookDetailPage.jsx
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance'; // API 호출을 위한 인스턴스

const BookDetailPage = () => {
    const { bookId } = useParams(); // URL에서 책 ID 가져오기
    const [book, setBook] = useState(null);

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

    if (!book) {
        return <div>Loading...</div>; // 로딩 중 표시
    }

    return (
        <div className="book-detail-page">
            <h1>{book.title}</h1>
            <div className="book-details">
                <img src={book.imageUrl} alt={book.title} />
                <div className="book-info">
                    <h2>{book.title}</h2>
                    <p><strong>Author:</strong> {book.author}</p>
                    <p><strong>Price:</strong> ${book.price}</p>
                    <p><strong>Description:</strong> {book.description}</p>
                </div>
            </div>
        </div>
    );
};

export default BookDetailPage;
